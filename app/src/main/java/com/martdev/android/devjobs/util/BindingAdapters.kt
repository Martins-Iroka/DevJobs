package com.martdev.android.devjobs.util

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.devjobresult.DevJobAdapter
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: PagedList<DevJob>?) {
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

fun ImageView.bindImage2(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.images))
                .into(this)
    }
}

@BindingAdapter("loadingStatus")
fun bindSwipeView(statusView: SwipeRefreshLayout, result: Result<List<DevJob>>?) {
    when (result?.status) {
        Result.Status.LOADING -> {
            statusView.isRefreshing = true
            statusView.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent)
        }
        Result.Status.SUCCESS -> statusView.isRefreshing = false
        Result.Status.ERROR -> statusView.isRefreshing = false
    }
}

//@BindingAdapter("statusTextView")
//fun bindTextView(statusView: TextView, networkState: Result<DevJob>) {
//    when(networkState.status) {
//        DevJobApiStatus.LOADING -> statusView.visibility = View.GONE
//
//        DevJobApiStatus.INTERNET_ERROR -> {
//            statusView.run {
//                visibility = View.VISIBLE
//                text = statusView.context.getString(R.string.no_internet_message)
//            }
//        }
//        DevJobApiStatus.LIST_ERROR -> {
//            statusView.run {
//                visibility = View.VISIBLE
//                text = statusView.context.getString(R.string.no_job_available_msg)
//            }
//        }
//
//        DevJobApiStatus.DONE -> statusView.visibility = View.GONE
//        Result.Status.SUCCESS -> TODO()
//        Result.Status.ERROR -> TODO()
//        Result.Status.LOADING -> TODO()
//    }
//}

fun TextView.formatHtml(description: String?) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = description?.let { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY) }
    } else {
        this.text = description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
    }
}
