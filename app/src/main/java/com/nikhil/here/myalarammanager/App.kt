package com.nikhil.here.myalarammanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.nikhil.here.myalarammanager.domain.notification.AlarmNotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AlarmNotificationService.ALARM_NOTIFICATION_CHANNEL_ID,
                AlarmNotificationService.ALARM_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = AlarmNotificationService.ALARM_NOTIFICATION_CHANNEL_DESCRIPTION
            }
            val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.createNotificationChannel(channel)
        }
    }
}