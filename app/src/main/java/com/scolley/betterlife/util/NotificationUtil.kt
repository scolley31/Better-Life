package com.scolley.betterlife.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavDeepLinkBuilder
import com.google.api.ResourceProto.resource
import com.google.firebase.auth.FirebaseAuth
import com.scolley.betterlife.MainActivity
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.timer.AppConstants
import com.scolley.betterlife.timer.TimerNotificationActionReceiver
import com.scolley.betterlife.timer.item.TimerItemFragment
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App Timer"
        private const val TIMER_ID = 0

        var plan = Plan()
        var planTeam = PlanForShow()
        var selectedTypeRadio = 0

        fun showTimerExpired(context: Context) {
//            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
//            startIntent.action = AppConstants.ACTION_START
//            val startPendingIntent = PendingIntent.getBroadcast(context,

            val bundle = Bundle()
            plan.dailyRemainTime = 1
            bundle.putParcelable("planKey", plan)
            bundle.putParcelable("planTeam", planTeam)

            Log.d("bundle", "bundle = $bundle")
            Log.d("bundle", "plan = $plan")
            Log.d("bundle", "planTeam = $planTeam")

            val pendingIntent = NavDeepLinkBuilder(context)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.navigation)
                    .setDestination(R.id.timerFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(
                    if (plan != null) {
                        "${plan.name}" + " 完成了"
                    } else {
                        "${planTeam.name}" + " 完成了"
                    }
            )
                    .setContentText("快去送出紀錄吧")
                    .setContentIntent(pendingIntent)
//                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
//                .addAction(R.drawable.logo, "Start", startPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerRunning(context: Context, wakeUpTime: Long, bundle: Bundle) {
//            val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
//            stopIntent.action = AppConstants.ACTION_STOP
//            val stopPendingIntent = PendingIntent.getBroadcast(context,
//                0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//            val pauseIntent = Intent(context, TimerNotificationActionReceiver::class.java)
//            pauseIntent.action = AppConstants.ACTION_PAUSE
//            val pausePendingIntent = PendingIntent.getBroadcast(context,
//                0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val pendingIntent = NavDeepLinkBuilder(context)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.navigation)
                    .setDestination(R.id.timerFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(
                    if (bundle.getParcelable<Plan>("planKey") != null) {
                        "${bundle.getParcelable<Plan>("planKey")?.name.toString()}" + " 記時中"
                    } else {
                        "${bundle.getParcelable<PlanForShow>("planTeam")?.name.toString()}" + " 記時中"
                    }
            )
                    .setContentText("截止時間: ${df.format(Date(wakeUpTime))}")
                    .setContentIntent(pendingIntent)
//                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                    .setOngoing(true)
//                .addAction(R.drawable._02_struggle, "Start", stopPendingIntent)
//                .addAction(R.drawable._01_arm_wrestling, "Pause", pausePendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerRunningNoLimit(context: Context, bundle: Bundle) {

            val pendingIntent = NavDeepLinkBuilder(context)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.navigation)
                    .setDestination(R.id.timerFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(
                    if (bundle.getParcelable<Plan>("planKey") != null) {
                        "${bundle.getParcelable<Plan>("planKey")?.name.toString()}" + " 記時中"
                    } else {
                        "${bundle.getParcelable<PlanForShow>("planTeam")?.name.toString()}" + " 記時中"
                    }
            )
                    .setContentText("無截止時間")
                    .setContentIntent(pendingIntent)
//                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                    .setOngoing(true)
//                .addAction(R.drawable._02_struggle, "Start", stopPendingIntent)
//                .addAction(R.drawable._01_arm_wrestling, "Pause", pausePendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }


        fun showTimerPaused(context: Context, bundle: Bundle) {
//            val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
//            resumeIntent.action = AppConstants.ACTION_RESUME
//            val resumePendingIntent = PendingIntent.getBroadcast(context,
//                0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val pendingIntent = NavDeepLinkBuilder(context)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.navigation)
                    .setDestination(R.id.timerFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(
                    if (bundle.getParcelable<Plan>("planKey") != null) {
                        "${bundle.getParcelable<Plan>("planKey")?.name.toString()}" + " 時間暫停"
                    } else {
                        "${bundle.getParcelable<PlanForShow>("planTeam")?.name.toString()}" + " 時間暫停"
                    }
            )
                    .setContentText("要繼續嗎?")
                    .setContentIntent(pendingIntent)
//                .setContentIntent(getPendingIntentWithStack(context, TimerItemFragment::class.java))
                    .setOngoing(true)
//                .addAction(R.drawable._01_arm_wrestling, "Resume", resumePendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun hideTimerNotification(context: Context) {
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        private fun getBasicNotificationBuilder(context: Context, channelId: String, playSound: Boolean)
                : NotificationCompat.Builder {
            val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val resources = context.resources
            val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
            val nBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true)
                    .setDefaults(0)
            if (playSound) nBuilder.setSound(notificationSound)
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context, javaClass: Class<T>): PendingIntent {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP


//            val pendingIntent = NavDeepLinkBuilder(context)
//                .setComponentName(MainActivity::class.java)
//                .setGraph(R.navigation.navigation)
//                .setDestination(R.id.timerFragment)
//                .setArguments(bundle)
//                .createPendingIntent()

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(channelID: String,
                                                                  channelName: String,
                                                                  playSound: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelID, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }
    }
}
