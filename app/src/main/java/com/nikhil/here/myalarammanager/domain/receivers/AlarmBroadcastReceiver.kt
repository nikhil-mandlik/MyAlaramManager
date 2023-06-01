package com.nikhil.here.myalarammanager.domain.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nikhil.here.myalarammanager.data.MyDatabase
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import com.nikhil.here.myalarammanager.domain.notification.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver  : BroadcastReceiver() {

    companion object {
        private const val TAG = "ExactAlarm"
    }

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    @Inject
    lateinit var myDatabase: MyDatabase


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG, "exception $throwable ")
    }

    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)



    override fun onReceive(context: Context?, intent: Intent?) {
        val actualTriggerTime = System.currentTimeMillis()
        val title = intent?.getStringExtra(MyAlarmManager.ALARM_TITLE) ?: "unknown alarm"
        val triggerTimeStamp  = intent?.getLongExtra(MyAlarmManager.ALARM_TIMESTAMP, -1) ?: -1L
        val isExact  = intent?.getBooleanExtra(MyAlarmManager.ALARM_TYPE_EXACT_OR_INEXACT, false) ?: false
        val allowWhileIdle  = intent?.getBooleanExtra(MyAlarmManager.ALARM_STATE_ALLOW_WHILE_IDLE, false) ?: false
        val id  = intent?.getLongExtra(MyAlarmManager.ALARM_ID, -1L) ?: -1L

        val extras = intent?.extras
        if (extras != null) {
            for (key in extras.keySet()) {
                val value = extras.get(key)
                Log.i(TAG, "onReceive: $key : $value")
            }
        }


        val delay = triggerTimeStamp.let {
            if (it >= 0) {
                actualTriggerTime - it
            } else {
                null
            }
        }
        val notifyDescription = "isExact : $isExact\nallowWhileIdle : $allowWhileIdle\nscheduleTime $triggerTimeStamp\nactualTriggerTime : $actualTriggerTime\ndelay : $delay ms"


        alarmNotificationService.showNotification(
            title = title,
            description = notifyDescription,
            pendingIntentReqCode = actualTriggerTime.toInt(),
            notifyId = id.toInt()
        )

        runBlocking {
            try {
                myDatabase.alarmDao().updateAlarm(
                    isTriggered = true,
                    triggerDelay = delay?:-1L,
                    id = id
                )
            } catch (e : Exception) {
                Log.i(TAG, "onReceive: $e")
            }
        }
    }
}