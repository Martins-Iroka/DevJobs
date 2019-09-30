package com.martdev.android.devjobs.network

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "dev_job")
data class DevJob(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String?,

        @ColumnInfo(name = "type")
        val type: String?,

        @ColumnInfo(name = "url")
        val url: String?,

        @Ignore
        @Json(name = "created_at") val createdAt: String?,
        val company: String?,

        @Ignore
        @Json(name = "company_url") val companyUrl: String?,

        @ColumnInfo(name = "location")
        val location: String?,

        @ColumnInfo(name = "title")
        val title: String?,

        @ColumnInfo(name = "description")
        val description: String?,

        @Ignore
        @Json(name = "how_to_apply") val howToApply: String?,

        @ColumnInfo(name = "logo_url")
        @Json(name = "company_logo") val companyLogo: String?
) : Parcelable