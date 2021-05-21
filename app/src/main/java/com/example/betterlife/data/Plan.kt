package com.example.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Plan(

        var id: String = "",
        var name: String = "",
        var category: String = "",
        var image: String? = "",
        var createdTime: Long = -1,
        var members: List<String> = listOf(),
        var target: Int = 0,
        var dailyTarget: Int = 0,
        var progressTime: Int = 0
//        var completedList: List<Completed> = listOf()

): Parcelable {

}

@Parcelize
data class Completed(

        var id: String = "",
        var user_id: String = "",
        var completed: Boolean = false,
        var daily: Int = 0,
        var date: Long = -1

) : Parcelable {



}

@Parcelize
data class Progress(
        var user_id: String = "",
        var percent: Float = 0.0f
) : Parcelable


