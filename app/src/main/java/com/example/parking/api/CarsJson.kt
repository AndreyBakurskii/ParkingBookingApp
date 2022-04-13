package com.example.parking.api

data class CarsJson(
    val id: String,
    val model: String,
    val registryNumber: String,
    val wight: Int,
    val length: Int,
)