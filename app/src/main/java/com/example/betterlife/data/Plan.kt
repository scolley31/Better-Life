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
        var createdTime: Long = -1,
        var members: List<String> = listOf(),
        var target: Int = 0, //min
        var dailyTarget: Int = 0, //min
        var progressTime: Int = 0, // min
        var taskDone: Boolean = false, //whether a history task or not
        var todayDone: Boolean = false, //whether finish the task today
        var group: Boolean = false, //whether is a group
        var dueDate: Long = 0

): Parcelable {

}

@Parcelize
data class PlanForShow(

        var id: String = "",
        var name: String = "",
        var category: String = "",
        var createdTime: Long = -1,
        var members: List<String> = listOf(),
        var target: Int = 0, //min
        var dailyTarget: Int = 0, //min
        var progressTimeOwn: Int = 0, // min
        var progressTimePartner: Int = 0, // min
        var progressTimeTotal: Int = 0, // m
        var taskDone: Boolean = false, //whether a history task or not
        var todayDone: Boolean = false, //whether finish the task today
        var group: Boolean = false, //whether is a group
        var dueDate: Long = 0

): Parcelable {

}

@Parcelize
data class Groups(

        var id: String = "",
        var membersID: List<String> = listOf()

): Parcelable {

}

@Parcelize
data class Completed(

        var id: String = "",
        var user_id: String = "",
        var completed: Boolean = false,
        var daily: Int = 0, //sec
        var date: Long = -1

) : Parcelable {

}

@Parcelize
data class Rank(
        var user_id: String = "",
        var totalTime: Int = 0
) : Parcelable


