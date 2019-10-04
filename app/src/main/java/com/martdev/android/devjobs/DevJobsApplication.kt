package com.martdev.android.devjobs

import android.app.Application
import timber.log.Timber

class DevJobsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}