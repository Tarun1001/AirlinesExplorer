package com.example.airlinesexplorer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.airlinesexplorer.domain.Airline


@Entity(tableName = "airlines")
data class AirlineEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val country: String,
    val headquarters: String,
    val fleetSize: Int,
    val website: String,
    val logoUrl: String,
    val isFavorite: Boolean = false
)

fun AirlineEntity.toAirline(): Airline {
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

fun Airline.toEntity(): AirlineEntity {
    return AirlineEntity(
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