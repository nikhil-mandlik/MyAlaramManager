package com.nikhil.here.myalarammanager.domain.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import com.nikhil.here.myalarammanager.domain.notification.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver  : BroadcastReceiver() {


    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    companion object {
        private const val TAG = "ExactAlarmBroadcastRece"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive: extras ${intent?.extras}")
        val actualTriggerTime = System.currentTimeMillis()
        val title = intent?.getStringExtra(MyAlarmManager.ALARM_TITLE)
        val triggerTimeStamp  = intent?.getLongExtra(MyAlarmManager.ALARM_TIMESTAMP, -1)
        val isExact  = intent?.getBooleanExtra(MyAlarmManager.ALARM_TYPE_EXACT_OR_INEXACT, false)
        val allowWhileIdle  = intent?.getBooleanExtra(MyAlarmManager.ALARM_STATE_ALLOW_WHILE_IDLE, false)
        val delay = triggerTimeStamp?.let {
            if (it >= 0) {
                actualTriggerTime - it
            } else {
                null
            }
        }
        val notifyTitle = title ?: "unknown alarm"
        val notifyDescription = "isExact : $isExact\nallowWhileIdle : $allowWhileIdle\nscheduleTime $triggerTimeStamp\nactualTriggerTime : $actualTriggerTime\ndelay : $delay"
        alarmNotificationService.showNotification(
            title = notifyTitle,
            description = notifyDescription,
            pendingIntentReqCode = actualTriggerTime.toInt(),
            notifyId = actualTriggerTime.toInt()
        )
    }
}