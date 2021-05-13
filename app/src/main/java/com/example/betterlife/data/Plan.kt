package com.example.betterlife.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Plan(

        val id: String,
        val name: String,
        val category: String,
        val image: String?,
        val createdTime: String,
        val members: List<String>,
        val type: String,
        val target: Int,
        val completedList: List<Completed>

): Parcelable

@Parcelize
data class Completed(

        val id: String,
        val user_id: String,
        val isCompleted: Boolean,
        val daily: Long,
        val date: String

) : Parcelable


