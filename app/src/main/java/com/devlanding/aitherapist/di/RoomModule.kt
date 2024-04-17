package com.devlanding.aitherapist.di

import android.content.Context
import androidx.room.Room
import com.devlanding.aitherapist.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun threadDao(appDatabase: AppDatabase) = appDatabase.threadDao()

    @Singleton
    @Provides
    fun roomInstance(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    ).build()

}