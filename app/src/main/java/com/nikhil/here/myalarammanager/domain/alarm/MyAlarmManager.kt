package com.nikhil.here.myalarammanager.domain.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.nikhil.here.myalarammanager.domain.receivers.AlarmBroadcastReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val TAG = "MyAlarmManager"
        const val ALARM_INTENT_REQUEST_CODE = 1
        const val ALARM_TITLE = "alarmTitle"
        const val ALARM_TIMESTAMP = "alarmTimestamp"
        const val ALARM_TYPE_EXACT_OR_INEXACT = "alarmTypeExactOrInExact"
        const val ALARM_STATE_ALLOW_WHILE_IDLE = "allowWhileIdle"
        const val ALARM_ID = "myAlarmId"
    }

    private var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(alarm: AlarmData) {
        val pendingIntent = createAlarmIntent(alarmData = alarm)
        if (alarm.isExact) {
            if (alarm.allowWhileIdle) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC,
                    alarm.triggerTimestamp,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC,
                    alarm.triggerTimestamp,
                    pendingIntent
                )
            }
        } else {
            if (alarm.allowWhileIdle) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC,
                    alarm.triggerTimestamp,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC,
                    alarm.triggerTimestamp,
                    pendingIntent
                )
            }
        }

    }

    fun cancelAlarm(alarmData: AlarmData) {
        Log.i(TAG, "cancelAlarm: $alarmData")
    }


    fun canScheduleExactAlarm(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }


    private fun createAlarmIntent(alarmData: AlarmData): PendingIntent {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(ALARM_TITLE, alarmData.title)
            putExtra(ALARM_TIMESTAMP, alarmData.triggerTimestamp)
            putExtra(ALARM_TYPE_EXACT_OR_INEXACT, alarmData.isExact)
            putExtra(ALARM_STATE_ALLOW_WHILE_IDLE, alarmData.allowWhileIdle)
            putExtra(ALARM_ID, alarmData.id)
        }
        return PendingIntent.getBroadcast(
            context,
            alarmData.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }


    fun rescheduleAlarms(alarms : List<AlarmData>) {
        alarms.map {
            scheduleAlarm(it)
        }
    }
}