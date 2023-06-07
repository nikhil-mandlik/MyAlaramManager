package com.nikhil.here.myalarammanager.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData

class MyTypeConverters {

    private val gson: Gson = Gson()

    @TypeConverter
    fun convertMetaDataToString(metaData: AlarmData.MetaData?): String? {
        return metaData?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun convertStringToMetaData(value: String?): AlarmData.MetaData? {
        return value?.let {
            gson.fromJson(it, AlarmData.MetaData::class.java)
        }
    }
}