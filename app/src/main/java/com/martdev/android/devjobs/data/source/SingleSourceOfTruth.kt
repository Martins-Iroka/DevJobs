package com.martdev.android.devjobs.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.PagedList
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.Result.Status.ERROR
import com.martdev.android.devjobs.data.Result.Status.SUCCESS
import kotlinx.coroutines.Dispatchers

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Result.Status.SUCCESS] - with data from database
 * [Result.Status.ERROR] - if error has occurred from any source
 * [Result.Status.LOADING]
 */
fun <T, A> resultLiveData(localDatabase: () -> LiveData<T>,
                          remoteDataSource: suspend () -> Result<A>,
                          saveRemoteResult: suspend (A) -> Unit): LiveData<Result<T>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading<T>())
            val localSource = localDatabase.invoke().map { Result.success(it) }
            emitSource(localSource)

            val responseStatus = remoteDataSource.invoke()
            when {
                responseStatus.status == SUCCESS -> saveRemoteResult(responseStatus.data!!)
                responseStatus.status == ERROR -> {
                    emit(Result.error<T>(responseStatus.message!!))
                    emitSource(localSource)
                }
            }
        }

data class SourceResult(val data: LiveData<PagedList<DevJob>>,
                        val networkState: LiveData<Result<List<DevJob>>>? = null)