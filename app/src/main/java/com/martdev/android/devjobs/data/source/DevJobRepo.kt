package com.martdev.android.devjobs.data.source

import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success
import com.martdev.android.devjobs.data.source.local.DevJobLocalDataSource
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class DevJobRepo(
        private val remote: DevJobRemoteDataSource,
        private val local: DevJobLocalDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DevJobRepository {

    override suspend fun getDevJobs(keyword: String): Result<List<DevJob>> {
        return withContext(ioDispatcher) {

            return@withContext when(val newJobs = fetchJobsFromRemoteOrLocal(keyword)) {
                is Success -> Success(newJobs.data)
                is Error -> Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchJobsFromRemoteOrLocal(keyword: String): Result<List<DevJob>> {
        when(val remoteJobs = remote.getDevJobs(keyword)) {
            is Error -> Timber.e("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteJobs.data)
                Timber.i("Remote data successful")
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
            return@withContext fetchJobFromRemoteOrLocal(jobId)
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
}