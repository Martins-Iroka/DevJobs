package com.martdev.android.devjobs

import android.content.Context
import com.martdev.android.devjobs.data.source.DevJobRepository
import com.martdev.android.devjobs.data.source.local.DevJobDatabase
import com.martdev.android.devjobs.data.source.local.DevJobLocalDataSource
import com.martdev.android.devjobs.data.source.network.DevJobApi
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource

object Injectors {

    fun provideDevJobRepository(context: Context): DevJobRepository {
        synchronized(this) {
            return DevJobRepository(createRemoteDataSource(), createLocalDataSource(context))
        }
    }

    private fun createRemoteDataSource(): DevJobRemoteDataSource {
        val jobApiService = DevJobApi.retrofitService
        return DevJobRemoteDataSource(jobApiService)
    }

    private fun createLocalDataSource(context: Context): DevJobLocalDataSource {
        val devJobDao = DevJobDatabase.getDatabase(context).devJobDao
        return DevJobLocalDataSource(devJobDao)
    }
}