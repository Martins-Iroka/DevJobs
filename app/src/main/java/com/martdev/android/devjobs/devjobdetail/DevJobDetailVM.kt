package com.martdev.android.devjobs.devjobdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.network.DevJob

class DevJobDetailVM(devJob: DevJob) : ViewModel() {

    private val _devJob = MutableLiveData<DevJob>()

    val devJob: LiveData<DevJob>
        get() = _devJob

    private val _navigate = MutableLiveData<DevJob>()

    val navigateToWebPage: LiveData<DevJob>
        get() = _navigate

    init {
        _devJob.value = devJob
    }

    fun navigateToWeb() {
        _navigate.value = _devJob.value
    }

    fun navigatedToWebPage() {
        _navigate.value = null
    }
}