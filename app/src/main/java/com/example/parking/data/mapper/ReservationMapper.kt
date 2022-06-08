package com.example.parking.data.mapper

import com.example.parking.data.models.Reservation
import com.example.parking.data.network.modelJSON.ReservationJson
import com.example.parking.utils.toDate
import java.util.*
import kotlin.collections.HashMap

//class ReservationMapper {
//    fun fromJsonToModel(jsonModel: ReservationJson): Reservation {
//        return Reservation(
//            id = UUID.fromString(jsonModel.id),
//            carId = UUID.fromString(jsonModel.carId),
//            parkingSpotId = UUID.fromString(jsonModel.parkingSpotId),
//            employeeId = UUID.fromString(jsonModel.employeeId),
//            startTime = jsonModel.startTime.toDate(),
//            endTime = jsonModel.endTime.toDate(),
//        )
//    }
//
//    fun fromHashMapToModel(hashMapModel: HashMap<String, String>): Reservation {
//        return Reservation(
//            id = UUID.fromString(hashMapModel["id"].toString()),
//            carId = UUID.fromString(hashMapModel["carId"].toString()),
//            parkingSpotId = UUID.fromString(hashMapModel["parkingSpotId"].toString()),
//            employeeId = UUID.fromString(hashMapModel["employeeId"].toString()),
//            startTime = hashMapModel["startTime"].toString().toDate(),
//            endTime = hashMapModel["endTime"].toString().toDate()
//        )
//    }
//}