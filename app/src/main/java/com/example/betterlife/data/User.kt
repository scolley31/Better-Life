package com.example.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

        val user_id: String,
        val user_name: String,
        val google_id: String

): Parcelable

