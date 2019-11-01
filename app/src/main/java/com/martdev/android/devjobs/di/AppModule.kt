package com.martdev.android.devjobs.di

import android.content.Context
import androidx.room.Room
import com.martdev.android.devjobs.data.source.local.DevJobDatabase
import com.martdev.android.devjobs.data.source.local.DevJobLocalDataSource
import com.martdev.android.devjobs.data.source.network.DevJobService
import com.martdev.android.devjobs.data.source.network.DevJobRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(jobApi: DevJobService): DevJobRemoteDataSource {
        return DevJobRemoteDataSource(jobApi)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(database: DevJobDatabase): DevJobLocalDataSource {
        return DevJobLocalDataSource(database.devJobDao)
    }

    @Synchronized
    @Singleton
    @Provides
    fun provideDataBase(context: Context): DevJobDatabase {
        return Room.databaseBuilder(context.applicationContext,
                DevJobDatabase::class.java, "DevJobs").build()
    }
}