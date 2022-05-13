package com.example.parking.api.dataclasses

data class ErrorJson(
    val description: String,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)