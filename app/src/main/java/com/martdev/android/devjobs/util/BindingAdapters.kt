package com.martdev.android.devjobs.util

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.devjobresult.DevJobAdapter
import com.martdev.android.devjobs.devjobresult.DevJobApiStatus
import com.martdev.android.devjobs.devjobrepo.network.DevJob

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<DevJob>?) {
    val adapter = recyclerView.adapter as DevJobAdapter
    data?.let { adapter.submitList(it) }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.images))
                .into(imgView)
    }
}

@BindingAdapter("loadingStatus")
fun bindSwipeView(statusView: SwipeRefreshLayout, status: DevJobApiStatus?) {
    when (status) {
        DevJobApiStatus.LOADING -> {
            statusView.isRefreshing = true
            statusView.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent)
        }
        DevJobApiStatus.INTERNET_ERROR -> statusView.isRefreshing = false

        DevJobApiStatus.LIST_ERROR -> statusView.isRefreshing = false

        DevJobApiStatus.DONE -> statusView.isRefreshing = false
    }
}

@BindingAdapter("statusTextView")
fun bindTextView(statusView: TextView, status: DevJobApiStatus?) {
    when(status) {
        DevJobApiStatus.LOADING -> statusView.visibility = View.GONE

        DevJobApiStatus.INTERNET_ERROR -> {
            statusView.run {
                visibility = View.VISIBLE
                text = statusView.context.getString(R.string.no_internet_message)
            }
        }
        DevJobApiStatus.LIST_ERROR -> {
            statusView.run {
                visibility = View.VISIBLE
                text = statusView.context.getString(R.string.no_job_available_msg)
            }
        }

        DevJobApiStatus.DONE -> statusView.visibility = View.GONE
    }
}

@BindingAdapter("description")
fun bindDescriptionView(descriptionView: TextView, description: String) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        descriptionView.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
    } else {
        descriptionView.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
