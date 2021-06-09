package com.scolley.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

        var userId: String = "",
        var userName: String = "",
        var userImage: String = ""

): Parcelable

