package com.nikhil.here.myalarammanager.domain.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "my_alarm")
data class AlarmData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("triggerTimestamp")
    val triggerTimestamp: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("isExact")
    val isExact: Boolean,
    @SerializedName("allowWhileIdle")
    val allowWhileIdle: Boolean,
    @SerializedName("isTriggered")
    val isTriggered: Boolean = false,
    @SerializedName("triggerDelay")
    val triggerDelay: Long? = null,
    @SerializedName("dateTimeString")
    val dateTimeString: String,
    @SerializedName("alarmMode")
    val alarmMode: AlarmMode,
    @SerializedName("executionMetaData")
    val executionMetaData: MetaData?,
    @SerializedName("triggerMetaData")
    val scheduleMetaData: MetaData?
) {
    data class MetaData(
        @SerializedName("isInDozeMode")
        val isInDozeMode: Boolean,
        @SerializedName("appStandbyBucket")
        val appStandbyBucket : String
    )
}


enum class AlarmMode {
    @SerializedName("ALARM_MANAGER")
    ALARM_MANAGER,

    @SerializedName("WORK_MANAGER")
    WORK_MANAGER
}

