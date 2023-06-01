package com.nikhil.here.myalarammanager.domain.di

import android.content.Context
import androidx.room.Room
import com.nikhil.here.myalarammanager.data.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ): MyDatabase {
        return Room.databaseBuilder(app, MyDatabase::class.java, MyDatabase.DB_NAME).build()
    }
}