package com.martdev.android.devjobs.devjobrepo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martdev.android.devjobs.devjobrepo.network.DevJob

@Database(entities = [DevJob::class], version = 1, exportSchema = false)
abstract class DevJobDatabase : RoomDatabase() {

    abstract val devJobDao: DevJobDao

    companion object {
        private var INSTANCE: DevJobDatabase? = null

        fun getDatabase(context: Context): DevJobDatabase {
            synchronized(DevJobDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            DevJobDatabase::class.java, "DevJobs").build()
                }
                return INSTANCE!!
            }
        }
    }
}