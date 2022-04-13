package com.example.parking.api

data class ReservationsJson(
    val id: String,
    val carId: String,
    val employeeId: String,
    val parkingSpotId: String,
    val startTime: String,
    val endTime: String,
)