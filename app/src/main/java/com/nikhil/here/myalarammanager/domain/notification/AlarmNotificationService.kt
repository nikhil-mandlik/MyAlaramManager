package com.nikhil.here.myalarammanager.domain.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.nikhil.here.myalarammanager.MainActivity
import com.nikhil.here.myalarammanager.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {


    companion object {
        const val ALARM_NOTIFICATION_CHANNEL_ID = "local_alarm_notification_channel"
        const val ALARM_NOTIFICATION_CHANNEL_NAME = "Local Alarm Notification"
        const val ALARM_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Channel use for triggering custom local notifications"
    }


    private val notifyManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun showNotification(
        title: String,
        description: String,
        pendingIntentReqCode: Int,
        notifyId: Int
    ) {

        val pendingIntent = PendingIntent.getActivity(
            context,
            pendingIntentReqCode,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_adb)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle())
            .build()

        notifyManager.notify(notifyId, notification)

    }
}