package com.martdev.android.devjobs.data.source.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.source.network.DevJobService.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()
//
//private val retrofit = Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .baseUrl(BASE_URL)
//        .build()

interface DevJobService {

    companion object {
        const val BASE_URL = "https://jobs.github.com/"
    }

    @GET("positions.json")
    fun getDevJobsAsync(@Query("description") keyword: String,
                        @Query("page") page: Int,
                        @Query("page_size") pageSize: Int):
            Deferred<Response<List<DevJob>>>

    @GET("positions/{ID}.json")
    fun getDevJobAsync(@Path("ID") id: String): Deferred<Response<DevJob>>
}

//object DevJobApi {
//    val retrofitService: DevJobService by lazy { retrofit.create(DevJobService::class.java) }
//}