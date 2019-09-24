package com.martdev.android.devjobs.devjobsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DevJobSearchVM : ViewModel() {

    val keyword = MutableLiveData<String>()

    val searchKeyword: LiveData<String> = keyword

    private val _showNoEntry = MutableLiveData<Boolean>()

    val showNoEntry: LiveData<Boolean>
        get() = _showNoEntry

    private val _navigateToResult = MutableLiveData<Boolean>()

    val navigateToResult: LiveData<Boolean> = _navigateToResult

    fun submitEntry() {

        if (keyword.value.isNullOrBlank()) {
            _showNoEntry.value = true
            return
        }

        _navigateToResult.value = true
    }

    fun disableLiveData() {
        _showNoEntry.value = null
        _navigateToResult.value = null
    }
}