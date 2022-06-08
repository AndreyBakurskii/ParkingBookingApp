package com.example.parking.data.network.modelJSON

data class ReservationJson(
    val id: String,
    val carId: String,
    val employeeId: String,
    val parkingSpotId: String,
    val startTime: String,
    val endTime: String,
)