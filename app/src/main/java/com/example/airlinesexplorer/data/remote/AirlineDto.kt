package com.example.airlinesexplorer.data.remote

import com.example.airlinesexplorer.domain.Airline
import com.google.gson.annotations.SerializedName

data class AirlineDto(
    val id: String,
    val name: String,
    val country: String,
    val headquarters: String,
    @SerializedName("fleet_size")
    val fleetSize: Int,
    val website: String,
    @SerializedName("logo_url")
    val logoUrl: String
)

fun AirlineDto.toAirline(isFavorite: Boolean = false): Airline {
    return Airline(
        id = id,
        name = name,
        country = country,
        headquarters = headquarters,
        fleetSize = fleetSize,
        website = website,
        logoUrl = logoUrl,
        isFavorite = isFavorite
    )
}