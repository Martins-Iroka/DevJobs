package com.martdev.android.devjobs.devjobdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.source.DevJobRepository

class DevJobDetailVM(jobId: String, repo: DevJobRepository) : ViewModel() {

    var devJob: LiveData<Result<DevJob>> = repo.getDevJob(jobId)

    private val _navigate = MutableLiveData<DevJob>()

    val navigateToWebPage: LiveData<DevJob>
        get() = _navigate

    fun navigateToWeb() {
        _navigate.value = devJob.value?.data
    }

    fun navigatedToWebPage() {
        _navigate.value = null
    }
}