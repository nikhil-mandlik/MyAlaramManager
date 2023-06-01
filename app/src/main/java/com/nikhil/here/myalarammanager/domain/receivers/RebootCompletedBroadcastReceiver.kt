package com.nikhil.here.myalarammanager.domain.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nikhil.here.myalarammanager.data.MyDatabase
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import com.nikhil.here.myalarammanager.domain.notification.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class RebootCompletedBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    @Inject
    lateinit var myDatabase: MyDatabase

    @Inject
    lateinit var myAlarmManager: MyAlarmManager

    companion object {
        private const val TAG = "RebootCompleted"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive: extras ${intent?.extras}")
        val rebootTimestamp = System.currentTimeMillis()
        alarmNotificationService.showNotification(
            title = "Reboot Completed",
            description = "rescheduling all the alarms",
            pendingIntentReqCode = 121,
            notifyId = 121
        )
        runBlocking {
            val pendingAlarms = myDatabase.alarmDao().getPendingAlarms().first()
            myAlarmManager.rescheduleAlarms(pendingAlarms)
        }
    }
}