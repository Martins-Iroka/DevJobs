package com.martdev.android.devjobs.data.source

import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import androidx.paging.LivePagedListBuilder
import com.martdev.android.devjobs.data.source.local.DevJobLocalDataSource
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource
import com.martdev.android.devjobs.data.source.paging.DevJobPageDataSourceFactory
import kotlinx.coroutines.CoroutineScope

class DevJobRepository(
        private val remote: DevJobRemoteDataSource,
        private val local: DevJobLocalDataSource) {

    fun getDevJobs(keyword: String, scope: CoroutineScope, networkConnected: Boolean): SourceResult {
        return if (networkConnected) getRemoteDevJobs(keyword, scope)
        else getLocalDevJobs()
    }

    private fun getRemoteDevJobs(keyword: String, scope: CoroutineScope): SourceResult {
        val factory = DevJobPageDataSourceFactory(keyword, local, remote, scope)

        val jobPageList =
                LivePagedListBuilder(factory, DevJobPageDataSourceFactory.pagedListConfig()).build()

        val networkState = Transformations.switchMap(factory.liveData) {
            it.network
        }

        return SourceResult(jobPageList, networkState)
    }

    private fun getLocalDevJobs(): SourceResult {
        val jobPageList =
                LivePagedListBuilder(local.getDevJobs(), DevJobPageDataSourceFactory.pagedListConfig()).build()

        return SourceResult(jobPageList)
    }


    fun getDevJob(jobId: String) = resultLiveData(
            localDatabase = {local.getDevJob(jobId)},
            remoteDataSource = {remote.getDevJob(jobId)},
            saveRemoteResult = {local.saveDevJob(it)}
    )


    suspend fun deleteDevJobs() {
        local.deleteDevJobs()
    }
}