package com.example.parking.api.dataclasses

data class ParkingSpotJson(
    val id: String,
    val parkingNumber: Int,
    val isFree: Boolean,
)