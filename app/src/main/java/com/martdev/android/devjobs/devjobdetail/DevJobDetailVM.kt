package com.martdev.android.devjobs.devjobdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.DevJobRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import com.martdev.android.devjobs.data.Result.Success as Success

class DevJobDetailVM(jobId: String, private val repo: DevJobRepository)
    : ViewModel() {


    private val job = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _devJob = MutableLiveData<DevJob>()

    val devJob: LiveData<DevJob>
        get() = _devJob

    private val _navigate = MutableLiveData<DevJob>()

    val navigateToWebPage: LiveData<DevJob>
        get() = _navigate

    val snackMessage = MutableLiveData<Int>()

    init {
        getDevJob(jobId)
    }

    private fun getDevJob(jobId: String){
        uiScope.launch {
            repo.getDevJob(jobId).let { result ->
                if (result is Success){
                    _devJob.value = result.data
                } else {
                    Timber.e("GetDevJob error")
                }
            }
        }
    }

    fun navigateToWeb() {
        _navigate.value = _devJob.value
    }

    fun navigatedToWebPage() {
        _navigate.value = null
    }

    fun snackMessageShown() {
        snackMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}