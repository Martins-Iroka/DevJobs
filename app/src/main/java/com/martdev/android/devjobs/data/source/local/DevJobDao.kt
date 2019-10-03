package com.martdev.android.devjobs.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martdev.android.devjobs.data.DevJob

@Dao
interface DevJobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevJob(devJob: DevJob)

    @Query("SELECT * FROM dev_job")
    suspend fun getDevJobs(): List<DevJob>

    @Query("SELECT * FROM dev_job WHERE id = :id")
    suspend fun getDevJob(id: String): DevJob?

    @Query("DELETE FROM dev_job")
    suspend fun deleteDevJobs()
}