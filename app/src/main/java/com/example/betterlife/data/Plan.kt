package com.example.betterlife.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Plan(

        var id: String = "",
        var name: String = "",
        var category: String = "",
        var image: String? = "",
        var createdTime: Long = -1,
        var members: List<String> = listOf(),
        var target: Long = 0,
        var dailyTarget: Int = 0,
        var progressTime: Long = 0
//        var completedList: List<Completed> = listOf()

): Parcelable {
    var targetToInt: Int = target.toInt()
    var progressTimeToInt : Int = progressTime.toInt()
}

@Parcelize
data class Completed(

        var id: String = "",
        var user_id: String = "",
        var completed: Boolean = false,
        var daily: Int = 0,
        var date: Timestamp = Timestamp(Date())

) : Parcelable

@Parcelize
data class Progress(
        var user_id: String = "",
        var percent: Float = 0.0f
) : Parcelable


