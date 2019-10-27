package com.martdev.android.devjobs.util

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import timber.log.Timber

object ConnectivityUtil {

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}

//private const val CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
//class ConnectivityUtil(private val context: Context) : LiveData<Boolean>() {
//
//    private var connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback
//
//    override fun onActive() {
//        super.onActive()
//        updateConnection()
//        when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
//                connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
//            else ->
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//                    context.registerReceiver(networkReceiver, IntentFilter(CONNECTIVITY_CHANGE))
//        }
//    }
//
//    override fun onInactive() {
//        super.onInactive()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
//        } else {
//            context.unregisterReceiver(networkReceiver)
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun lollipopNetworkAvailableRequest() {
//        val builder = NetworkRequest.Builder()
//                .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
//        connectivityManager.registerNetworkCallback(builder.build(), getConnectivityManagerCallback())
//    }
//
//    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
//                override fun onLost(network: Network?) {
//                    super.onLost(network)
//                    postValue(false)
//                }
//
//                override fun onAvailable(network: Network?) {
//                    super.onAvailable(network)
//                    postValue(true)
//                    Timber.i("Connected")
//                }
//            }
//            return connectivityManagerCallback
//        } else {
//            throw IllegalAccessError("Error Occurred")
//        }
//    }
//
//    private val networkReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            updateConnection()
//        }
//    }
//
//    private fun updateConnection() {
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        postValue(activeNetwork?.isConnected == true)
//    }
//}


