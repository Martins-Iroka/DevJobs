package com.martdev.android.devjobs.data.source.local

import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success
import com.martdev.android.devjobs.data.source.DevJobDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DevJobLocalDataSource internal constructor(
        private val devJobDao: DevJobDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
)
    : DevJobDataSource {


    override suspend fun getDevJobs(keyword: String): Result<List<DevJob>> = withContext(ioDispatcher) {
            return@withContext try {
                Success(devJobDao.getDevJobs())
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun getDevJob(jobId: String): Result<DevJob> = withContext(ioDispatcher) {
            try {
                val devJob = devJobDao.getDevJob(jobId)
                if (devJob != null) return@withContext Success(devJob)
                else return@withContext Error(Exception("Job not found!"))
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun saveDevJob(job: DevJob) = withContext(ioDispatcher) {
        devJobDao.insertDevJob(job)
    }

    override suspend fun deleteDevJobs() = withContext(ioDispatcher) {
        devJobDao.deleteDevJobs()
    }
}