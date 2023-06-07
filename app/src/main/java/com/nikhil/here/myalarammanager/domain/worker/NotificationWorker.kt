package com.nikhil.here.myalarammanager.domain.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.nikhil.here.myalarammanager.data.MyDatabase
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import com.nikhil.here.myalarammanager.domain.notification.AlarmNotificationService
import com.nikhil.here.myalarammanager.ui.DozeModeAndAppStandByChecker
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val notificationService: AlarmNotificationService,
    private val myDatabase: MyDatabase
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "NotificationWorker"
    }

    override suspend fun doWork(): Result {
        try {
            Log.i(TAG, "doWork: is called with inputData ${workerParams.inputData}")
            val actualTriggerTime = System.currentTimeMillis()
            val title = workerParams.inputData.getString(MyAlarmManager.ALARM_TITLE) ?: "unknown alarm"
            val triggerTimeStamp = workerParams.inputData.getLong(MyAlarmManager.ALARM_TIMESTAMP, -1)
            val isExact =
                workerParams.inputData.getBoolean(MyAlarmManager.ALARM_TYPE_EXACT_OR_INEXACT, false)
            val allowWhileIdle =
                workerParams.inputData.getBoolean(MyAlarmManager.ALARM_STATE_ALLOW_WHILE_IDLE, false)
            val id = workerParams.inputData.getLong(MyAlarmManager.ALARM_ID, -1L)
            val delay = triggerTimeStamp.let {
                if (it >= 0) {
                    actualTriggerTime - it
                } else {
                    null
                }
            }

            val notifyDescription =
                "isExact : $isExact\nallowWhileIdle : $allowWhileIdle\nscheduleTime $triggerTimeStamp\nactualTriggerTime : $actualTriggerTime\ndelay : $delay ms"


            notificationService.showNotification(
                title = title,
                description = notifyDescription,
                pendingIntentReqCode = actualTriggerTime.toInt(),
                notifyId = id.toInt()
            )

            myDatabase.alarmDao().updateAlarm(
                isTriggered = true,
                triggerDelay = delay ?: -1L,
                id = id,
                executionMetaData = AlarmData.MetaData(
                    isInDozeMode = DozeModeAndAppStandByChecker.isInDozeMode(context = appContext),
                    appStandbyBucket = DozeModeAndAppStandByChecker.getAppStandbyBucket(context = appContext)
                )
            )

            return Result.success(
                workDataOf(
                    MyAlarmManager.ALARM_ID to id
                )
            )
        } catch(e : Exception) {
            Log.i(TAG, "doWork: $e")
            return Result.failure(
                workDataOf(
                    "EXCEPTION" to e
                )
            )
        }
    }


}