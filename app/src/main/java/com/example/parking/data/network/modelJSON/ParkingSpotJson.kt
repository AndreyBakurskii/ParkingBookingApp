package com.example.parking.data.network.modelJSON

data class ParkingSpotJson(
    val id: String,
    val parkingNumber: Int,
    val isFree: Boolean,
)