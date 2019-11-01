package com.martdev.android.devjobs.devjobresult

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.DevJobRepository
import com.martdev.android.devjobs.data.source.SourceResult
import javax.inject.Inject

class DevJobResultVM @Inject constructor(private val repo: DevJobRepository) : ViewModel() {

    private val _navigateToJobDetail = MutableLiveData<String>()

    val navigateToJobDetail: LiveData<String>
        get() = _navigateToJobDetail

    private val _searchKeyword = MutableLiveData<String>()

    val searchKeyword: LiveData<String> = _searchKeyword

    var connectivityAvailable: Boolean = false

    private val result: LiveData<SourceResult> = Transformations.map(_searchKeyword) {
        repo.getDevJobs(it, viewModelScope, connectivityAvailable)
    }

    val devJobs: LiveData<PagedList<DevJob>>
        get() = Transformations.switchMap(result) {
            it.data
        }

    val networkState = Transformations.switchMap(result) {
        it.networkState
    }

    fun showResult(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun showJobDetails(jobId: String) {
        _navigateToJobDetail.value = jobId
    }

    fun jobDetailShown() {
        _navigateToJobDetail.value = null
    }

    fun searchForJob(keyword: String) {
        _searchKeyword.value = keyword
    }
}