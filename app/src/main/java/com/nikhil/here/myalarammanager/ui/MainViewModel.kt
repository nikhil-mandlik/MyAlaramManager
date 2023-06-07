package com.nikhil.here.myalarammanager.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.nikhil.here.myalarammanager.data.MyDatabase
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import com.nikhil.here.myalarammanager.domain.worker.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.TimeUnit
import javax.inject.Inject


data class MainState(
    val alarms: List<AlarmData> = emptyList()
)

sealed class MainSideEffects {

}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmManager: MyAlarmManager,
    private val myDatabase: MyDatabase,
    @ApplicationContext private val applicationContext: Context
) : ViewModel(), ContainerHost<MainState, MainSideEffects> {

    override val container: Container<MainState, MainSideEffects> = container(MainState())

    private val workManager = WorkManager.getInstance(applicationContext)

    init {
        observeDatabase()
    }

    private fun observeDatabase() {
        intent {
            myDatabase.alarmDao().fetchAlarms().collectLatest {
                Log.i("ExactAlarm", "observeDatabase: received update $it")
                reduce {
                    state.copy(
                        alarms = it
                    )
                }
            }
        }
    }

    fun scheduleNotification(
        alarm: AlarmData
    ) {
        intent {
            val id = myDatabase.alarmDao().insert(alarmData = alarm)
            alarmManager.scheduleAlarm(alarm = alarm.copy(id = id))
        }
    }


    fun deleteTriggeredAlarms() {
        intent {
            myDatabase.alarmDao().deleteTriggeredAlarms()
        }
    }


    fun hasExactAlarmPermission() = alarmManager.canScheduleExactAlarm()


    fun scheduleNotificationThroughWorkManager(
        alarm: AlarmData
    ) {
        intent {
            val id = myDatabase.alarmDao().insert(alarmData = alarm)
            val updatedAlarm = alarm.copy(
                id = id
            )
            val delay = updatedAlarm.triggerTimestamp - System.currentTimeMillis()

            val workRequest = OneTimeWorkRequest
                .Builder(NotificationWorker::class.java)
                .setInputData(
                    workDataOf(
                        MyAlarmManager.ALARM_TITLE to updatedAlarm.title,
                        MyAlarmManager.ALARM_TIMESTAMP to updatedAlarm.triggerTimestamp,
                        MyAlarmManager.ALARM_TYPE_EXACT_OR_INEXACT to updatedAlarm.isExact,
                        MyAlarmManager.ALARM_STATE_ALLOW_WHILE_IDLE to updatedAlarm.allowWhileIdle,
                        MyAlarmManager.ALARM_ID to updatedAlarm.id,
                    )
                )
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueue(workRequest)

        }
    }

}