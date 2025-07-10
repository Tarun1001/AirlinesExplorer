package com.example.airlinesexplorer.di

// DatabaseModule.kt

import android.content.Context
import androidx.room.Room
import com.example.airlinesexplorer.data.local.AirlineDao
import com.example.airlinesexplorer.data.local.AirlineDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAirlineDatabase(@ApplicationContext context: Context): AirlineDatabase {
        return Room.databaseBuilder(
            context,
            AirlineDatabase::class.java,
            AirlineDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideAirlineDao(database: AirlineDatabase): AirlineDao {
        return database.airlineDao()
    }
}