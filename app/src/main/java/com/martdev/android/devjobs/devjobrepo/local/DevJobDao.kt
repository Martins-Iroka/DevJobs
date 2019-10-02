package com.martdev.android.devjobs.devjobrepo.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.martdev.android.devjobs.devjobrepo.network.DevJob

@Dao
interface DevJobDao {

    @Insert
    fun insert(devJob: DevJob)

    @Query("SELECT * FROM dev_job WHERE bookmarked = 1")
    fun get(): LiveData<List<DevJob>>

    @Query("DELETE FROM dev_job WHERE bookmarked = 0")
    fun deleteDevJob()
}