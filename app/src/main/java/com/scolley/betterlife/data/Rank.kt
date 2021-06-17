package com.scolley.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rank(
        var user_id: String = "",
        var userName: String = "",
        var totalTime: Int = 0,
        var targetRate: Int = 0,
        var userImage: String = ""
) : Parcelable
