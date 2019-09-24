package com.martdev.android.devjobs.devjobdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.martdev.android.devjobs.DevJobFactory
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.databinding.DevjobDetailFragmentBinding

class DevJobDetail : Fragment() {

    private lateinit var binding: DevjobDetailFragmentBinding

    private lateinit var viewModel: DevJobDetailVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.devjob_detail_fragment, container, false)

        val devJobArg = DevJobDetailArgs.fromBundle(arguments!!).devJob

        val factory = DevJobFactory(devJob = devJobArg)

        viewModel = ViewModelProviders.of(this, factory)[DevJobDetailVM::class.java]

        binding.devJobVM = viewModel
        binding.lifecycleOwner = this

        viewModel.devJob.observe(this, Observer {
            it?.let {
                binding.toolbar.title = it.title
            }
        })

        return binding.root
    }
}