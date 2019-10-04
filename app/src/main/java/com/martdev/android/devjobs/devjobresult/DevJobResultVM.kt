package com.martdev.android.devjobs.devjobresult

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.DevJobRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import com.martdev.android.devjobs.data.Result.Success as Success

enum class DevJobApiStatus { LOADING, INTERNET_ERROR, LIST_ERROR, DONE }

class DevJobResultVM(keyword: String, private val repo: DevJobRepository) : ViewModel() {

    private val _status = MutableLiveData<DevJobApiStatus>()

    val status: LiveData<DevJobApiStatus>
        get() = _status

    private val _devJobs =
            MutableLiveData<List<DevJob>>().apply { value = emptyList() }

    val devJobs: LiveData<List<DevJob>>
        get() = _devJobs

    private val _navigateToJobDetail = MutableLiveData<String>()

    val navigateToJobDetail: LiveData<String>
        get() = _navigateToJobDetail

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getDevJobs(keyword)
    }

    private fun getDevJobs(keyword: String) {
        uiScope.launch {
            _status.value = DevJobApiStatus.LOADING

            val devJobResult = repo.getDevJobs(keyword)
            if (devJobResult is Success) {
                Log.i("DevJobVM", "is Success")
                _status.value = DevJobApiStatus.DONE
                val jobs = devJobResult.data
                if (jobs.isEmpty()) DevJobApiStatus.LIST_ERROR
                else _devJobs.value = jobs
            } else {
                Log.e("DevJobVM", "is Error")
                _status.value = DevJobApiStatus.INTERNET_ERROR
                _devJobs.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showJobDetails(jobId: String) {
        _navigateToJobDetail.value = jobId
    }

    fun jobDetailShown() {
        _navigateToJobDetail.value = null
    }

    fun searchForJob(keyword: String) = getDevJobs(keyword)
}