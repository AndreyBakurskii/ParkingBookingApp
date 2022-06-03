package com.example.parking.data.mapper

import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.network.modelJSON.ParkingSpotJson
import java.util.*

class ParkingSpotMapper {
    fun fromJsonToModel(jsonModel: ParkingSpotJson): ParkingSpot {
        return ParkingSpot(
            id = UUID.fromString(jsonModel.id),
            parkingNumber = jsonModel.parkingNumber,
            isFree = jsonModel.isFree
        )
    }

    fun fromHashMapToModel(hashMapModel: HashMap<String, Any>): ParkingSpot {
        return ParkingSpot(
            id = UUID.fromString(hashMapModel["id"].toString()),
            parkingNumber = hashMapModel["parkingNumber"] as Int ,
            isFree = hashMapModel["isFree"] as Boolean
        )
    }
}