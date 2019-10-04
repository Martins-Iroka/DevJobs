package com.martdev.android.devjobs.data.source.network

import android.util.Log
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Error
import com.martdev.android.devjobs.data.Result.Success

class DevJobRemoteDataSource(private val jobApi: DevJobApiService) {

    suspend fun getDevJobs(keyword: String): Result<List<DevJob>> {
        return try {
            val result = jobApi.getDevJobsAsync(keyword).await()
            when {
                result.isNullOrEmpty() ->{
                    Log.e("remote data source", "Local datasource list is empty")
                    Error(Exception("remote datasource list is empty"))
                }
                else -> Success(result)
            }
        } catch (e: Exception) {
            Log.e("remote data source", "remote datasource error")
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