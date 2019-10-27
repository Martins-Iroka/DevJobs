package com.martdev.android.devjobs.data.source.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.local.DevJobLocalDataSource
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource
import kotlinx.coroutines.CoroutineScope

class DevJobPageDataSourceFactory(private val keyword: String,
                                  private val localSource: DevJobLocalDataSource,
                                  private val remoteSource: DevJobRemoteDataSource,
                                  private val scope: CoroutineScope)
    : DataSource.Factory<Int, DevJob>() {

    val liveData = MutableLiveData<DevJobPageDataSource>()

    override fun create(): DataSource<Int, DevJob> {
        val source = DevJobPageDataSource(keyword, localSource, remoteSource, scope)
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 50

        fun pagedListConfig() = PagedList.Config.Builder()
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(true)
                .build()
    }
}