package com.scolley.betterlife.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.scolley.betterlife.home.item.HomeTeamViewModel
import com.scolley.betterlife.timer.item.TimerItemFragment
import com.scolley.betterlife.timer.item.TimerItemViewModel
import com.scolley.betterlife.timer.item.TimerStatus
import com.scolley.betterlife.util.NotificationUtil
import com.scolley.betterlife.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        NotificationUtil.showTimerExpired(context)
        PrefUtil.setTimerState(TimerStatus.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}