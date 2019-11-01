package com.martdev.android.devjobs.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martdev.android.devjobs.data.DevJob

@Database(entities = [DevJob::class], version = 1, exportSchema = false)
abstract class DevJobDatabase : RoomDatabase() {

    abstract val devJobDao: DevJobDao

}