package com.martdev.android.devjobs.devjobrepo.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://jobs.github.com/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

interface DevJobApiService {

    @GET("positions.json")
    fun getDevJobsAsync(@Query("description") keyword: String):
            Deferred<List<DevJob>>
}

object DevJobApi {
    val retrofitService: DevJobApiService by lazy { retrofit.create(DevJobApiService::class.java) }
}