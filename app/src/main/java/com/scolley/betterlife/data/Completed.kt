package com.scolley.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Completed(

        var id: String = "",
        var user_id: String = "",
        var completed: Boolean = false,
        var daily: Int = 0, //sec
        var date: Long = -1

) : Parcelable