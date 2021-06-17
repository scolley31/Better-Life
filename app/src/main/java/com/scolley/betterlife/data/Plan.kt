package com.scolley.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
        var dueDate: Long = 0,
        var dailyRemainTime: Int = 0, // for timer notification
        var selectedTypeRadio: Int = 0, // for time limit or time target
        var dailyCountTime: Int = 0

): Parcelable

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
        var ownerID: String = "",
        var progressTimePartner: Int = 0, // min
        var partnerID: String = "",
        var progressTimeTotal: Int = 0, // m
        var taskDone: Boolean = false, //whether a history task or not
        var todayDone: Boolean = false, //whether finish the task today
        var group: Boolean = false, //whether is a group
        var dueDate: Long = 0,
        var dailyRemainTime: Int = 0, // for timer notification
        var selectedTypeRadio: Int = 0, // for time limit or time target
        var dailyCountTime: Int = 0

): Parcelable




