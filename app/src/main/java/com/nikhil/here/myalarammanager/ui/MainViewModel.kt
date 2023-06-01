package com.nikhil.here.myalarammanager.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.nikhil.here.myalarammanager.data.MyDatabase
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


data class MainState(
    val alarms: List<AlarmData> = emptyList()
)

sealed class MainSideEffects {

}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmManager: MyAlarmManager,
    private val myDatabase: MyDatabase
) : ViewModel(), ContainerHost<MainState, MainSideEffects> {

    override val container: Container<MainState, MainSideEffects> = container(MainState())

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

}