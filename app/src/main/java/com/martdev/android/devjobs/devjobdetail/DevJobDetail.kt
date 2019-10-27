package com.martdev.android.devjobs.devjobdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.martdev.android.devjobs.DevJobFactory
import com.martdev.android.devjobs.Injectors
import com.martdev.android.devjobs.webpage.DevJobWebPage
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.databinding.DevjobDetailFragmentBinding
import com.martdev.android.devjobs.util.bindImage2
import com.martdev.android.devjobs.util.formatHtml

class DevJobDetail : AppCompatActivity() {

    private val devJobArg: DevJobDetailArgs by navArgs()

    private lateinit var binding: DevjobDetailFragmentBinding

    private val viewModel: DevJobDetailVM by viewModels {
        DevJobFactory(jobId = devJobArg.jobId, repository = Injectors.provideDevJobRepository(application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.devjob_detail_fragment)

        binding.devJobVM = viewModel
        binding.lifecycleOwner = this

        viewModel.devJob.observe(this, Observer {result ->
            when(result.status) {
                Result.Status.LOADING -> TODO() //TODO fix this
                Result.Status.SUCCESS -> {
                    binding.toolbar.title = result.data?.title
                    handleCollapsedToolbarTitle(result.data)
                    binding.logo.bindImage2(result.data?.companyLogo)
                    binding.jobDescription.formatHtml(result.data?.description)
                }
                Result.Status.ERROR -> Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.navigateToWebPage.observe(this, Observer { devJob ->
            if (devJob != null) {
                DevJobWebPage.newIntent(this, devJob.url.toUri()).also {
                    startActivity(it)
                }
                viewModel.navigatedToWebPage()
            }
        })
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.devjob_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_share -> {
                val devJob = viewModel.devJob.value?.data
                val jobUrl = devJob?.url!!.toUri().buildUpon().scheme("https").build()
                ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setSubject(devJob.title)
                        .setText("${devJob.company} is hiring. Check it out\n\n" + jobUrl)
                        .createChooserIntent().also { startActivity(it) }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun handleCollapsedToolbarTitle(job: DevJob?) {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout: AppBarLayout, i: Int ->
            var scrollRange = -1

            if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange

            if (scrollRange + i == 0) {
                binding.collapsingBar.title = job?.company
            } else {
                binding.collapsingBar.title = ""
            }
        })
    }
}