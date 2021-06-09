package com.scolley.betterlife.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.scolley.betterlife.PlanApplication

object Util {

    fun isInternetConnected(): Boolean {
        val cm = PlanApplication.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return PlanApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return PlanApplication.instance.getColor(resourceId)
    }
}
