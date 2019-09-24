package com.martdev.android.devjobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.martdev.android.devjobs.devjobdetail.DevJobDetailVM
import com.martdev.android.devjobs.devjobresult.DevJobResultVM
import com.martdev.android.devjobs.network.DevJob

class DevJobFactory(private val keyword: String = "",
                    private val devJob: DevJob? = null) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DevJobResultVM::class.java) -> DevJobResultVM(keyword) as T
            modelClass.isAssignableFrom(DevJobDetailVM::class.java) -> devJob?.let { DevJobDetailVM(it) } as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}