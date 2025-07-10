package com.example.airlinesexplorer.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [AirlineEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AirlineDatabase : RoomDatabase() {
    abstract fun airlineDao(): AirlineDao

    companion object {
        const val DATABASE_NAME = "airline_db"
    }
}