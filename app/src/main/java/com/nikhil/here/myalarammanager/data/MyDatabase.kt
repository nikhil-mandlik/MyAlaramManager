package com.nikhil.here.myalarammanager.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import kotlinx.coroutines.flow.Flow


@Database(
    entities = [AlarmData::class],
    version = 1,
    exportSchema = true
)
abstract class MyDatabase : RoomDatabase() {
    companion object {
        private const val TAG = "MyDatabase"
        const val DB_NAME = "my_alarm_db"
    }

    abstract fun alarmDao(): AlarmDao
}

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmData: AlarmData): Long

    @Query("SELECT * FROM `my_alarm` ORDER BY `triggerTimestamp` DESC")
    fun fetchAlarms(): Flow<List<AlarmData>>

    @Query("UPDATE `my_alarm` SET `isTriggered`=:isTriggered, `triggerDelay`=:triggerDelay WHERE `id`=:id")
    suspend fun updateAlarm(isTriggered: Boolean, triggerDelay: Long, id: Long)


    @Query("DELETE FROM `my_alarm` WHERE `isTriggered`=1")
    fun deleteTriggeredAlarms()


    @Query("SELECT * FROM `my_alarm` WHERE `isTriggered`=0")
    fun getPendingAlarms(): Flow<List<AlarmData>>



}