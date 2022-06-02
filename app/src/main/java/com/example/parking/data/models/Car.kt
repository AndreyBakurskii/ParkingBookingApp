package com.example.parking.data.models

import java.util.*
import kotlin.collections.HashMap


class Car (
    val id: UUID = UUID.fromString("0f29717c-37e4-4a93-9165-6baacae64e98"),
    var model: String,
    var length: Int = 400,
    var wight: Int = 160,
    var registryNumber: String) {

    fun toHashMap(withID: Boolean = false): HashMap<String, Any> {
        return if (withID) hashMapOf(
            "id" to id.toString(),
            "model" to model,
            "registryNumber" to registryNumber,
            "wight" to wight,
            "length" to length
        ) else
            hashMapOf(
                "model" to model,
                "registryNumber" to registryNumber,
                "wight" to wight,
                "length" to length
            )
    }
}