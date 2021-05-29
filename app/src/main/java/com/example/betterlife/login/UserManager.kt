package com.example.betterlife.login

import android.content.Context
import com.example.betterlife.PlanApplication

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_NAME = "user_name"
    private const val LAST_TIME_LOGIN_GOOGLE = "last_time_google"

    var userName: String? = null
        get() = PlanApplication.instance
                .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
                .getString(USER_NAME, "")
        set(value) {
            field = when (value) {
                null -> {
                    PlanApplication.instance
                            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                            .remove(USER_NAME)
                            .apply()
                    null
                }
                else -> {
                    PlanApplication.instance
                            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                            .putString(USER_NAME, value)
                            .apply()
                    value
                }
            }
        }


    var lastTimeGoogle: String? = null
        get() = PlanApplication.instance
                .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
                .getString(LAST_TIME_LOGIN_GOOGLE, "")
        set(value) {
            field = when (value) {
                null -> {
                    PlanApplication.instance
                            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                            .remove(LAST_TIME_LOGIN_GOOGLE)
                            .apply()
                    null
                }
                else -> {
                    PlanApplication.instance
                            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                            .putString(LAST_TIME_LOGIN_GOOGLE, value)
                            .apply()
                    value
                }
            }
        }
}