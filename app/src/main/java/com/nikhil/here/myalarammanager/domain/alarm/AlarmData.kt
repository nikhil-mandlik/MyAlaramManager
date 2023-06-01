package com.nikhil.here.myalarammanager.domain.alarm

data class AlarmData(
    val triggerTimestamp: Long,
    val title : String,
    val isExact : Boolean,
    val allowWhileIdle : Boolean,
    val isTriggered : Boolean = false,
    val triggerDelay : Long? = null
)

