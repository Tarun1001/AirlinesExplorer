package com.example.airlinesexplorer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface AirlineApiService {
    @GET("airlines")
    suspend fun getAirlines(): List<AirlineDto>

    @GET("airlines/{id}")
    suspend fun getAirlineById(@Path("id") id: String): AirlineDto
}