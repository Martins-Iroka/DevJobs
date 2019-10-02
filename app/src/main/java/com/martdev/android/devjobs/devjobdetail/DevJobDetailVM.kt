package com.martdev.android.devjobs.devjobdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.martdev.android.devjobs.devjobrepo.DevJobRepo
import com.martdev.android.devjobs.devjobrepo.local.DevJobDatabase
import com.martdev.android.devjobs.devjobrepo.network.DevJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DevJobDetailVM(devJob: DevJob, application: Application)
    : AndroidViewModel(application) {


    private val job = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _devJob = MutableLiveData<DevJob>()

    val devJob: LiveData<DevJob>
        get() = _devJob

    private val _navigate = MutableLiveData<DevJob>()

    val navigateToWebPage: LiveData<DevJob>
        get() = _navigate

    val snackMessage = MutableLiveData<Int>()

    private val database = DevJobDatabase.getDatabase(application)
    private var repo = DevJobRepo.getRepo(database)

    init {
        _devJob.value = devJob
    }

    var isBookmarked = _devJob.value?.isBookmarked!!

    fun navigateToWeb() {
        _navigate.value = _devJob.value
    }

    fun navigatedToWebPage() {
        _navigate.value = null
    }

    fun snackMessageShown() {
        snackMessage.value = null
    }

    fun bookmarkClicked() {
        uiScope.launch {
            if (!isBookmarked) {
                devJob.value?.isBookmarked = true
                repo.insertDevJob(devJob.value!!)
                snackMessage.value = 0
            } else {
                devJob.value?.isBookmarked = false
                repo.deleteDevJobs()
                snackMessage.value = 1
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}