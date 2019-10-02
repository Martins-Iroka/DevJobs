package com.martdev.android.devjobs.devjobrepo

import android.app.Application
import androidx.lifecycle.LiveData
import com.martdev.android.devjobs.devjobrepo.local.DevJobDatabase
import com.martdev.android.devjobs.devjobrepo.network.DevJob
import com.martdev.android.devjobs.devjobrepo.network.DevJobApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DevJobRepo private constructor(private val database: DevJobDatabase) {

    companion object {

        fun getRepo(database: DevJobDatabase): DevJobRepo {
            return DevJobRepo(database)
        }

        suspend fun getDevJobs(keyword: String): List<DevJob> {
            return DevJobApi.retrofitService.getDevJobsAsync(keyword).await()
        }
    }

    fun getBookmarkedJobs(): LiveData<List<DevJob>> = database.devJobDao.get()

    suspend fun insertDevJob(devJob: DevJob) {
        withContext(Dispatchers.IO) {
            database.devJobDao.insert(devJob)
        }
    }

    suspend fun deleteDevJobs() {
        withContext(Dispatchers.IO) {
            database.devJobDao.deleteDevJob()
        }
    }
}