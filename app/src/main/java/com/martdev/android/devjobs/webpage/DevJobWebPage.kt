package com.martdev.android.devjobs.webpage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.databinding.DevjobWebpageBinding

class DevJobWebPage : AppCompatActivity() {

    private lateinit var mUri: Uri
    private lateinit var mWebView: WebView
    private lateinit var mProgressBar: ProgressBar

    private lateinit var mBinding: DevjobWebpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.devjob_webpage)

        mUri = intent.data!!

        setSupportActionBar(mBinding.toolbar)

        mProgressBar = mBinding.progressBar
        mBinding.progressBar.max = 100

        mWebView = mBinding.webView

        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    mProgressBar.visibility = View.GONE
                } else {
                    mProgressBar.visibility = View.VISIBLE
                    mProgressBar.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                supportActionBar?.subtitle = mWebView.url
            }
        }
        mWebView.webViewClient = WebViewClient()
        mWebView.loadUrl(mUri.toString())
    }

    override fun onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        fun newIntent(context: Context, uri: Uri?): Intent = Intent(context, DevJobWebPage::class.java).apply {
            data = uri
        }

//        {
//            val intent = Intent(context, DevJobWebPage::class.java)
//            intent.data = uri
//            return intent
//        }
    }
}
