package com.martdev.android.devjobs.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "dev_job")
data class DevJob(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "type")
        var type: String = "",

        @ColumnInfo(name = "url")
        var url: String = "",

        @Ignore
        @Json(name = "created_at") var createdAt: String? = "",

        @ColumnInfo(name = "company")
        var company: String = "",

        @Ignore
        @Json(name = "company_url") var companyUrl: String? = "",

        @ColumnInfo(name = "location")
        var location: String = "",

        @ColumnInfo(name = "title")
        var title: String = "",

        @ColumnInfo(name = "description")
        var description: String = "",

        @Ignore
        @Json(name = "how_to_apply") var howToApply: String? = "",

        @ColumnInfo(name = "logo_url")
        @Json(name = "company_logo") var companyLogo: String? = ""
)