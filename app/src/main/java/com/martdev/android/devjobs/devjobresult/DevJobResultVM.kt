package com.martdev.android.devjobs.devjobresult

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.devjobrepo.DevJobRepo
import com.martdev.android.devjobs.devjobrepo.network.DevJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class DevJobApiStatus { LOADING, INTERNET_ERROR, LIST_ERROR, DONE }

class DevJobResultVM(keyword: String, application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData<DevJobApiStatus>()

    val status: LiveData<DevJobApiStatus>
        get() = _status

    private val _devJobs = MutableLiveData<List<DevJob>>()

    val devJobs: LiveData<List<DevJob>>
        get() = _devJobs

    private val _navigateToJobDetail = MutableLiveData<DevJob>()

    val navigateToJobDetail: LiveData<DevJob>
        get() = _navigateToJobDetail

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getDevJobs(keyword)
    }

    private fun getDevJobs(keyword: String) {
        uiScope.launch {
            try {
                _status.value = DevJobApiStatus.LOADING
                val devJobs = DevJobRepo.getDevJobs(keyword)
                _status.value = DevJobApiStatus.DONE
                if (devJobs.isNullOrEmpty()) _status.value = DevJobApiStatus.LIST_ERROR
                else _devJobs.value = devJobs
            } catch (e: Exception) {
                Log.e("Result", "The error message is " + e.message)
                _status.value = DevJobApiStatus.INTERNET_ERROR
                _devJobs.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showJobDetails(devJob: DevJob) {
        _navigateToJobDetail.value = devJob
    }

    fun jobDetailShown() {
        _navigateToJobDetail.value = null
    }

    fun searchForJob(keyword: String) = getDevJobs(keyword)
}