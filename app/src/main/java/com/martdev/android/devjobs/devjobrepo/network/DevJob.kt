package com.martdev.android.devjobs.devjobrepo.network

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "dev_job")
data class DevJob(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        var id: String?,

        @ColumnInfo(name = "type")
        var type: String?,

        @ColumnInfo(name = "url")
        var url: String?,

        @Ignore
        @Json(name = "created_at") var createdAt: String?,
        var company: String?,

        @Ignore
        @Json(name = "company_url") var companyUrl: String?,

        @ColumnInfo(name = "location")
        var location: String?,

        @ColumnInfo(name = "title")
        var title: String?,

        @ColumnInfo(name = "description")
        var description: String?,

        @Ignore
        @Json(name = "how_to_apply") var howToApply: String?,

        @ColumnInfo(name = "logo_url")
        @Json(name = "company_logo") var companyLogo: String?,

        @ColumnInfo(name = "bookmarked")
        var isBookmarked: Boolean = false
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(type)
                parcel.writeString(url)
                parcel.writeString(createdAt)
                parcel.writeString(company)
                parcel.writeString(companyUrl)
                parcel.writeString(location)
                parcel.writeString(title)
                parcel.writeString(description)
                parcel.writeString(howToApply)
                parcel.writeString(companyLogo)
                parcel.writeByte(if (isBookmarked) 1 else 0)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<DevJob> {
                override fun createFromParcel(parcel: Parcel): DevJob {
                        return DevJob(parcel)
                }

                override fun newArray(size: Int): Array<DevJob?> {
                        return arrayOfNulls(size)
                }
        }
}