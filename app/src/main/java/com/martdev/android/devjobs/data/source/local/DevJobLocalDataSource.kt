package com.martdev.android.devjobs.data.source.local

import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class DevJobLocalDataSource internal constructor(
        private val devJobDao: DevJobDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {


    suspend fun getDevJobs(): Result<List<DevJob>> = withContext(ioDispatcher) {
            return@withContext try {
                val devJobs = devJobDao.getDevJobs()
                when {
                    devJobs.isNullOrEmpty() -> {
                        Timber.e( "Local datasource list is empty")
                        Error(Exception("Local datasource list is empty"))
                    }
                    else -> Success(devJobs)
                }
            } catch (e: Exception) {
                Timber.e( "Local datasource error")
                Error(e)
            }
        }

    suspend fun getDevJob(jobId: String): Result<DevJob> = withContext(ioDispatcher) {
            try {
                val devJob = devJobDao.getDevJob(jobId)
                if (devJob != null) return@withContext Success(devJob)
                else return@withContext Error(Exception("Job not found!"))
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    suspend fun saveDevJob(job: DevJob) = withContext(ioDispatcher) {
        devJobDao.insertDevJob(job)
    }

    suspend fun deleteDevJobs() = withContext(ioDispatcher) {
        devJobDao.deleteDevJobs()
    }
}