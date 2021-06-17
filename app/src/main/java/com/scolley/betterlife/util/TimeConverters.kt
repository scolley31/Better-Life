package com.scolley.betterlife.util

import java.text.SimpleDateFormat
import java.util.*

object TimeConverters {

    @JvmStatic
    fun timeStampToTime(time: Long, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", locale)
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun timeToTimestamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", locale)
        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun dateToTimestamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun timestampToDate(date: Long, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        return simpleDateFormat.format(Date(date))
    }

    @JvmStatic
    fun timestampToDateNoYear(date: Long, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat("MM-dd", locale)
        return simpleDateFormat.format(Date(date))
    }

}