package com.martdev.android.devjobs.data.source.network

import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success
import timber.log.Timber

class DevJobRemoteDataSource(private val jobApi: DevJobApiService) {

    suspend fun getDevJobs(keyword: String): Result<List<DevJob>> {
        return try {
            val result = jobApi.getDevJobsAsync(keyword).await()
            when {
                result.isNullOrEmpty() ->{
                    Timber.e( "Local datasource list is empty")
                    Error(Exception("remote datasource list is empty"))
                }
                else -> Success(result)
            }
        } catch (e: Exception) {
            Timber.e( "remote datasource error")
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