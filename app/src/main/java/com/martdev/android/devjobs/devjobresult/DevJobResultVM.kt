package com.martdev.android.devjobs.devjobresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.DevJobRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DevJobResultVM(keyword: String, private val repo: DevJobRepository) : ViewModel() {

    private val _navigateToJobDetail = MutableLiveData<String>()

    val navigateToJobDetail: LiveData<String>
        get() = _navigateToJobDetail

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val searchKeyword = MutableLiveData<String>()

    var connectivityAvailable: Boolean = false

    private val result = Transformations.map(searchKeyword) {
        repo.getDevJobs(it, uiScope, connectivityAvailable)
    }

    val devJobs: LiveData<PagedList<DevJob>>
        get() = Transformations.switchMap(result) {
            it.data
        }

    val networkState = Transformations.switchMap(result) {
        it.networkState
    }

    init {
        searchKeyword.value = keyword
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

    fun searchForJob(keyword: String) {
        searchKeyword.value = keyword
    }
}