package com.scolley.betterlife.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.scolley.betterlife.timer.item.TimerItemFragment
import com.scolley.betterlife.timer.item.TimerItemViewModel
import com.scolley.betterlife.timer.item.TimerStatus
import com.scolley.betterlife.util.NotificationUtil
import com.scolley.betterlife.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
//            AppConstants.ACTION_STOP -> {
//                TimerItemFragment.removeAlarm(context)
//                PrefUtil.setTimerState(TimerStatus.Stopped, context)
//                NotificationUtil.hideTimerNotification(context)
//            }
//            AppConstants.ACTION_PAUSE -> {
//                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
//                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
//                val nowSeconds = TimerItemFragment.nowSeconds
//
//                secondsRemaining -= nowSeconds - alarmSetTime
//                PrefUtil.setSecondsRemaining(secondsRemaining, context)
//
//                TimerItemFragment.removeAlarm(context)
//                PrefUtil.setTimerState(TimerStatus.Paused, context)
//                NotificationUtil.showTimerPaused(context)
//            }
//            AppConstants.ACTION_RESUME -> {
//                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
//                val wakeUpTime = TimerItemFragment.setAlarm(context, TimerItemFragment.nowSeconds, secondsRemaining)
//                PrefUtil.setTimerState(TimerStatus.Running, context)
//                NotificationUtil.showTimerRunning(context, wakeUpTime)
//            }
//            AppConstants.ACTION_START -> {
//                val minutesRemaining = PrefUtil.getTimerLength(context)
//                val secondsRemaining = minutesRemaining * 60L
//                val wakeUpTime = TimerItemFragment.setAlarm(context, TimerItemFragment.nowSeconds, secondsRemaining)
//                PrefUtil.setTimerState(TimerStatus.Running, context)
//                PrefUtil.setSecondsRemaining(secondsRemaining, context)
//                NotificationUtil.showTimerRunning(context, wakeUpTime)
//            }
        }
    }
}
