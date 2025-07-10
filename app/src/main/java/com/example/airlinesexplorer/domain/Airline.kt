package com.example.airlinesexplorer.domain


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Airline(
    val id: String,
    val name: String,
    val country: String,
    val headquarters: String,
    val fleetSize: Int,
    val website: String,
    val logoUrl: String,
    val isFavorite: Boolean = false
) : Parcelable