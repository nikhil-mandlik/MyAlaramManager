package com.nikhil.here.myalarammanager.ui.navigation

sealed class NavGraph (val route : String) {
    object  HomeScreen : NavGraph("homeScreen")
    object  ScheduleAlarmScreen : NavGraph("scheduleAlarmScreen")
}
