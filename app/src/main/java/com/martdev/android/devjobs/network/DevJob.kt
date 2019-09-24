package com.martdev.android.devjobs.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DevJob(
        val id: String,
        val type: String,
        val url: String,
        @Json(name = "created_at") val createdAt: String,
        val company: String,
        @Json(name = "company_url") val companyUrl: String,
        val location: String,
        val title: String,
        val description: String,
        @Json(name = "how_to_apply") val howToApply: String,
        @Json(name = "company_logo") val companyLogo: String
        ) : Parcelable