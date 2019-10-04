package com.martdev.android.devjobs.data.source

import android.util.Log
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DevJobRepo(
        private val remote: DevJobRemoteDataSource,
        private val local: DevJobDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DevJobRepository {

    private var cachedJobs: ConcurrentMap<String, DevJob>? = null

    override suspend fun getDevJobs(keyword: String): Result<List<DevJob>> {
        return withContext(ioDispatcher) {
            cachedJobs?.let { cachedJobs ->
                Success(cachedJobs.values.sortedBy { it.id })
            }

            val newJobs = fetchJobsFromRemoteOrLocal(keyword)

            (newJobs as? Success)?.let { refreshCache(it.data) }

            cachedJobs?.values?.let { jobs ->
                return@withContext Success(jobs.sortedBy { it.id })
            }

            (newJobs as? Success)?.let {
                if (it.data.isNullOrEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(Exception("Illegal state"))
        }
    }

    private suspend fun fetchJobsFromRemoteOrLocal(keyword: String): Result<List<DevJob>> {
        when(val remoteJobs = remote.getDevJobs(keyword)) {
            is Error -> Log.w("DevJobRepo","Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteJobs.data)
                Log.i("DevJobRepo", "Remote data successful")
            }
            else -> throw IllegalStateException()
        }

        //Local if remote fails
        val localJobs = local.getDevJobs()
        if (localJobs is Success) return localJobs
        return Error(Exception("Error fetching from remote and local"))
    }

    private suspend fun refreshLocalDataSource(jobs: List<DevJob>) {
        local.deleteDevJobs()
        for (job in jobs) {
            local.saveDevJob(job)
        }
    }

    override suspend fun getDevJob(jobId: String): Result<DevJob> {
        return withContext(ioDispatcher) {
            cachedJobs?.get(jobId)?.let {
                Success(it)
            }

            val newJob = fetchJobFromRemoteOrLocal(jobId)

            //Refresh the cache with the new job
            (newJob as? Success)?.let { cacheJob(it.data) }

            return@withContext newJob
        }
    }

    private suspend fun fetchJobFromRemoteOrLocal(jobId: String): Result<DevJob> {


        when(val remoteJob = remote.getDevJob(jobId)) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteJob.data)
            }
            else -> throw IllegalStateException()
        }

        //Local if remote fails
        val localJob = local.getDevJob(jobId)
        if (localJob is Success) return localJob
        return Error(Exception("Error fetching from remote and local"))
    }

    private suspend fun refreshLocalDataSource(job: DevJob) {
        local.saveDevJob(job)
    }

    override suspend fun deleteDevJobs() {
        local.deleteDevJobs()
    }

    private fun refreshCache(jobs: List<DevJob>) {
        cachedJobs?.clear()
        jobs.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private inline fun cacheAndPerform(job: DevJob, perform: (DevJob) -> Unit) {
        val cachedJob = cacheJob(job)
        perform(cachedJob)
    }

    private fun cacheJob(job: DevJob): DevJob {
        val cacheJob = DevJob(id = job.id, type = job.type, url = job.url, createdAt = job.createdAt,
                company = job.company,companyUrl = job.companyUrl, location = job.location, title = job.title,
                description = job.description, howToApply = job.howToApply, companyLogo = job.companyLogo)
        if (cachedJobs == null) {
            cachedJobs = ConcurrentHashMap()
        }
        cachedJobs?.put(cacheJob.id, cacheJob)
        return cacheJob
    }
}