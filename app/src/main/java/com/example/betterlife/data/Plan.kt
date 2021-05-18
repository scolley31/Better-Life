package com.example.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
        var completedList: List<Completed> = listOf()

): Parcelable

@Parcelize
data class Completed(

        val id: String,
        val user_id: String,
        val isCompleted: Boolean,
        val daily: Int,
        val date: String

) : Parcelable


