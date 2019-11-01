package com.martdev.android.devjobs.data.source.network

import javax.inject.Inject


class DevJobRemoteDataSource @Inject constructor(private val jobApi: DevJobService) : BaseDataSource() {

    suspend fun getDevJobs(keyword: String, page: Int, pageSize: Int) = getResult { jobApi.getDevJobsAsync(keyword, page, pageSize).await() }

    suspend fun getDevJob(jobId: String) = getResult { jobApi.getDevJobAsync(id = jobId).await() }
}