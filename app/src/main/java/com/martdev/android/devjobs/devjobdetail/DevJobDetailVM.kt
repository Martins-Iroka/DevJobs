package com.martdev.android.devjobs.devjobdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.data.source.DevJobRepository
import javax.inject.Inject

class DevJobDetailVM @Inject constructor(repo: DevJobRepository) : ViewModel() {

    private var jobId: String = ""

    var devJob: LiveData<Result<DevJob>> = repo.getDevJob(jobId)

    private val _navigate = MutableLiveData<DevJob>()

    val navigateToWebPage: LiveData<DevJob>
        get() = _navigate

    fun showDetail(id: String) {
        jobId = id
    }

    fun navigateToWeb() {
        _navigate.value = devJob.value?.data
    }

    fun navigatedToWebPage() {
        _navigate.value = null
    }
}