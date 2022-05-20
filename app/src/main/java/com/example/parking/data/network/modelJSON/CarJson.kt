package com.example.parking.data.network.modelJSON

data class CarJson(
    val id: String,
    val model: String,
    val registryNumber: String,
    val wight: Int,
    val length: Int,
)