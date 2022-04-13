package com.example.parking.api

data class ParkingSpotJson(
    val id: String,
    val parkingNumber: Int,
    val isFree: Boolean,
)