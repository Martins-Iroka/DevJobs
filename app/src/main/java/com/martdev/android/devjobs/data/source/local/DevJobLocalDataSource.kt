package com.martdev.android.devjobs.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.martdev.android.devjobs.data.DevJob

class DevJobLocalDataSource internal constructor(private val devJobDao: DevJobDao) {

    fun getDevJobs(): DataSource.Factory<Int, DevJob> = devJobDao.getDevJobs()

    fun getDevJob(jobId: String): LiveData<DevJob> = devJobDao.getDevJob(jobId)

    suspend fun saveDevJob(job: DevJob) = devJobDao.insertDevJob(job)

    suspend fun saveDevJobs(jobs: List<DevJob>) = devJobDao.insertDevJobs(jobs)

    suspend fun deleteDevJobs() = devJobDao.deleteDevJobs()
}