package com.scolley.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Groups(

        var id: String = "",
        var membersID: List<String> = listOf()

): Parcelable
