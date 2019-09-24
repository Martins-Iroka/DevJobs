package com.martdev.android.devjobs.devjobdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.network.DevJob

class DevJobDetailVM(devJob: DevJob) : ViewModel() {

    private val _devJob = MutableLiveData<DevJob>()

    val devJob: LiveData<DevJob>
        get() = _devJob

    init {
        _devJob.value = devJob
    }
}