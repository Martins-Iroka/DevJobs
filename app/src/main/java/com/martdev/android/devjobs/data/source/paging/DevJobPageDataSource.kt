package com.martdev.android.devjobs.data.source.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.source.local.DevJobLocalDataSource
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class DevJobPageDataSource(private val keyword: String,
                           private val localSource: DevJobLocalDataSource,
                           private val remoteSource: DevJobRemoteDataSource,
                           private val scope: CoroutineScope) : PageKeyedDataSource<Int, DevJob>() {

    var network = MutableLiveData<Result<List<DevJob>>>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DevJob>) {
        fetchData(1, params.requestedLoadSize) {jobs ->
            callback.onResult(jobs, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DevJob>) {
        val page = params.key
        fetchData(page, params.requestedLoadSize) {jobs ->
            callback.onResult(jobs, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DevJob>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun fetchData(page: Int, pageSize: Int, callback: (List<DevJob>) -> Unit) {
        scope.launch {
            network.value = Result.loading()
            val response = remoteSource.getDevJobs(keyword, page, pageSize)
            when {
                response.status == Result.Status.SUCCESS -> {
                    val jobs = response.data!!
                    network.postValue(Result.success(jobs))
                    localSource.saveDevJobs(jobs)
                    callback(jobs)
                }
                response.status == Result.Status.ERROR -> {
                    postError(response.message)
                }
            }
        }
    }

    private fun postError(message: String?) {
        Timber.e("An error happened: $message")
        network.postValue(Result.error(message))
    }
}