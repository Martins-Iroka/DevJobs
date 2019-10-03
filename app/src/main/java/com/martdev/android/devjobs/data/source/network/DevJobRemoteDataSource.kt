package com.martdev.android.devjobs.data.source.network

import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success

class DevJobRemoteDataSource(private val jobApi: DevJobApiService) {

    suspend fun getDevJobs(keyword: String): Result<List<DevJob>> {
        return try {
            Success(jobApi.getDevJobsAsync(keyword).await())
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getDevJob(jobId: String): Result<DevJob> {
        return try {
            Success(jobApi.getDevJobAsync(id = jobId).await())
        } catch (e: Exception) {
            Error(e)
        }
    }
}