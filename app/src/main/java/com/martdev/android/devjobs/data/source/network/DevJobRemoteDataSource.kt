package com.martdev.android.devjobs.data.source.network


class DevJobRemoteDataSource(private val jobApi: DevJobApiService) : BaseDataSource() {

    suspend fun getDevJobs(keyword: String, page: Int, pageSize: Int) = getResult { jobApi.getDevJobsAsync(keyword, page, pageSize).await() }

    suspend fun getDevJob(jobId: String) = getResult { jobApi.getDevJobAsync(id = jobId).await() }
}