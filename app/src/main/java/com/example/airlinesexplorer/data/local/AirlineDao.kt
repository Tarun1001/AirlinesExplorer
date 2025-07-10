package com.example.airlinesexplorer.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AirlineDao {
    @Query("SELECT * FROM airlines")
    fun getAllAirlines(): Flow<List<AirlineEntity>>

    @Query("SELECT * FROM airlines WHERE id = :id")
    suspend fun getAirlineById(id: String): AirlineEntity?

    @Query("SELECT * FROM airlines WHERE name LIKE :query OR country LIKE :query")
    fun searchAirlines(query: String): Flow<List<AirlineEntity>>

    @Query("SELECT * FROM airlines WHERE isFavorite = 1")
    fun getFavoriteAirlines(): Flow<List<AirlineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirlines(airlines: List<AirlineEntity>)

    @Update
    suspend fun updateAirline(airline: AirlineEntity)

    @Query("DELETE FROM airlines")
    suspend fun deleteAll()
}