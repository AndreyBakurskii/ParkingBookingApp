package com.example.parking.api.dataclasses

data class CarJson(
    val id: String,
    val model: String,
    val registryNumber: String,
    val wight: Int,
    val length: Int,
)