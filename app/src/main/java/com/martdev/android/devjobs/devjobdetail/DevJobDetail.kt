package com.martdev.android.devjobs.devjobdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.devjobs.DevJobFactory
import com.martdev.android.devjobs.webpage.DevJobWebPage
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.databinding.DevjobDetailFragmentBinding

class DevJobDetail : AppCompatActivity() {

    private val devJobArg: DevJobDetailArgs by navArgs()

    private lateinit var binding: DevjobDetailFragmentBinding

    private lateinit var viewModel: DevJobDetailVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.devjob_detail_fragment)

        val factory = DevJobFactory(devJob = devJobArg.devJob, application = application)

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

        viewModel.snackMessage.observe(this, Observer {message ->
            when(message) {
                0 -> {
                    Snackbar.make(binding.root, "Added to Bookmark", Snackbar.LENGTH_LONG).show()
                    viewModel.snackMessageShown()
                }
                1 -> {
                    Snackbar.make(binding.root, "Removed from Bookmark", Snackbar.LENGTH_LONG).show()
                    viewModel.snackMessageShown()
                }
            }
        })

        handleCollapsedToolbarTitle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.devjob_details, menu)

        val bookmarkItem = menu.findItem(R.id.action_bookmark)
        if (viewModel.isBookmarked) {
            bookmarkItem.setIcon(R.drawable.ic_bookmark_black_24dp)
                    .title = "Remove from bookmark"
        } else {
            bookmarkItem.setIcon(R.drawable.ic_bookmark_border_black_24dp)
                    .title = "Add to bookmark"
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_share -> {
                val devJob = viewModel.devJob.value
                val jobUrl = devJob?.url!!.toUri().buildUpon().scheme("https").build()
                ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setSubject(devJob.title)
                        .setText("${devJob.company} is hiring. Check it out" + jobUrl)
                        .createChooserIntent()
                return true
            }

            R.id.action_bookmark -> {
                viewModel.bookmarkClicked()
                invalidateOptionsMenu()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
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