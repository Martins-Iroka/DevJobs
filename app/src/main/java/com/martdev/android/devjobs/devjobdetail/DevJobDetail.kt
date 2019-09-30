package com.martdev.android.devjobs.devjobdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.martdev.android.devjobs.DevJobFactory
import com.martdev.android.devjobs.DevJobWebPage
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.databinding.DevjobDetailFragmentBinding

class DevJobDetail : AppCompatActivity() {

    private val devJobArg: DevJobDetailArgs by navArgs()

    private lateinit var binding: DevjobDetailFragmentBinding

    private lateinit var viewModel: DevJobDetailVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.devjob_detail_fragment)

        val factory = DevJobFactory(devJob = devJobArg.devJob)

        viewModel = ViewModelProviders.of(this, factory)[DevJobDetailVM::class.java]

        binding.devJobVM = viewModel
        binding.lifecycleOwner = this

        viewModel.devJob.observe(this, Observer {
            it?.let {
                binding.toolbar.title = it.title
            }
        })

        viewModel.navigateToWebPage.observe(this, Observer { devJob ->
            if (devJob != null) {
                DevJobWebPage.newIntent(this, devJob.url?.toUri()).also {
                    startActivity(it)
                }
                viewModel.navigatedToWebPage()
            }
        })

        handleCollapsedToolbarTitle()
    }

    private fun handleCollapsedToolbarTitle() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout: AppBarLayout, i: Int ->
            var scrollRange = -1

            if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange

            if (scrollRange + i == 0) {
                binding.collapsingBar.title = viewModel.devJob.value?.company
            } else {
                binding.collapsingBar.title = ""
            }
        })
    }
}