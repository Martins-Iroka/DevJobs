package com.martdev.android.devjobs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.martdev.android.devjobs.devjobdetail.DevJobDetailVM
import com.martdev.android.devjobs.devjobresult.DevJobResultVM
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.DevJobRepository

class DevJobFactory(private val keyword: String = "",
                    private val jobId: String = "",
                    private val repository: DevJobRepository) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DevJobResultVM::class.java) -> DevJobResultVM(keyword, repository) as T
            modelClass.isAssignableFrom(DevJobDetailVM::class.java) -> DevJobDetailVM(jobId, repository)  as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}