package com.nikhil.here.myalarammanager.ui

import androidx.lifecycle.ViewModel
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.MyAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


data class MainState(
    val isLoading: Boolean = false
)

sealed class MainSideEffects {

}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmManager: MyAlarmManager
) : ViewModel(), ContainerHost<MainState, MainSideEffects> {

    override val container: Container<MainState, MainSideEffects> = container(MainState())


    fun scheduleNotification(
        alarm: AlarmData
    ) {
        alarmManager.scheduleAlarm(alarm = alarm)
    }

}