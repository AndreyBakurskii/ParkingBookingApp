package com.example.parking.data.models;

import java.util.UUID;

class ParkingSpot(
    val id: UUID = UUID.fromString("0f29717c-37e4-4a93-9165-6baacae64e98"),
    var parkingNumber: Int,
    var isFree: Boolean = true
) {

    fun toHashMap(withID: Boolean = false): HashMap<String, Any> {
        return if (withID) hashMapOf(
            "id" to id.toString(),
            "parkingNumber" to parkingNumber,
            "isFree" to isFree
        ) else
            hashMapOf(
                "parkingNumber" to parkingNumber,
                "isFree" to isFree
            )
    }
}
