package com.martdev.android.devjobs.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martdev.android.devjobs.data.DevJob

@Dao
interface DevJobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevJob(devJob: DevJob)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevJobs(devJobs: List<DevJob>)

    @Query("SELECT * FROM dev_job")
    fun getDevJobs(): DataSource.Factory<Int, DevJob>

    @Query("SELECT * FROM dev_job WHERE id = :id")
    fun getDevJob(id: String): LiveData<DevJob>

    @Query("DELETE FROM dev_job")
    suspend fun deleteDevJobs()
}