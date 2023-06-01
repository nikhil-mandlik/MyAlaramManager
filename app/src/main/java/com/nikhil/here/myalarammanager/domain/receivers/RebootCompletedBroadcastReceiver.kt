package com.nikhil.here.myalarammanager.domain.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nikhil.here.myalarammanager.domain.notification.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RebootCompletedBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    companion object {
        private const val TAG = "RebootCompleted"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive: extras ${intent?.extras}")
        val rebootTimestamp = System.currentTimeMillis()
        alarmNotificationService.showNotification(
            title = "Reboot Completed",
            description = "rescheduling all the alarms",
            pendingIntentReqCode = rebootTimestamp.toInt(),
            notifyId = rebootTimestamp.toInt()
        )
    }
}