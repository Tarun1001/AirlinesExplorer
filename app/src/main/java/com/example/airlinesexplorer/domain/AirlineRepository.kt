package com.example.airlinesexplorer.domain



import com.example.airlinesexplorer.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AirlineRepository {
    suspend fun getAirlines(): Flow<Resource<List<Airline>>>
    suspend fun getAirlineById(id: String): Flow<Resource<Airline>>
    suspend fun searchAirlines(query: String): Flow<Resource<List<Airline>>>
    suspend fun toggleFavorite(airlineId: String)
    suspend fun getFavoriteAirlines(): Flow<List<Airline>>
}