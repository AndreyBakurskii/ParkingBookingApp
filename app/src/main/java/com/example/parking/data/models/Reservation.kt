package com.example.parking.data.models;

import com.example.parking.utils.toStr
import java.text.SimpleDateFormat
import java.util.*


//class Reservation (
//    val id: UUID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
//    var carId: UUID,
//    var employeeId: UUID,
//    var parkingSpotId: UUID,
//    var startTime: Date,
//    var endTime: Date
//) {
//    private val dateFormat: SimpleDateFormat = SimpleDateFormat("", Locale.US)
//    init {
//        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
//    }
//
//    fun toHashMap(withID: Boolean = false) : HashMap<String, String> {
//        return if (withID) hashMapOf(
//            "id" to id.toString(),
//            "carId" to carId.toString(),
//            "employeeId" to employeeId.toString(),
//            "parkingSpot" to parkingSpotId.toString(),
//            "startTime" to startTime.toString(),
//            "endTime" to endTime.toString()
//        ) else hashMapOf(
//            "carId" to carId.toString(),
//            "employeeId" to employeeId.toString(),
//            "parkingSpot" to parkingSpotId.toString(),
//            "startTime" to startTime.toStr(),
//            "endTime" to endTime.toStr()
//        )
//    }
//}

class Reservation (
    val id: UUID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
    var car: Car,
    var employee: Employee,
    var parkingSpot: ParkingSpot,
    var startTime: Date,
    var endTime: Date
) {

    private val dateTimePattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    private val datePresentationPattern: String = "EEE dd/MM"
    private val timePresentationPattern: String = "HH:mm"

    fun toHashMap(withID: Boolean = false) : HashMap<String, String> {
        return if (withID) hashMapOf(
            "id" to id.toString(),
            "carId" to car.id.toString(),
            "employeeId" to employee.id.toString(),
            "parkingSpot" to parkingSpot.id.toString(),
            "startTime" to startTime.toStr(dateTimePattern),
            "endTime" to endTime.toStr(dateTimePattern)
        ) else hashMapOf(
            "carId" to car.id.toString(),
            "employeeId" to employee.id.toString(),
            "parkingSpot" to parkingSpot.id.toString(),
            "startTime" to startTime.toStr(dateTimePattern),
            "endTime" to endTime.toStr(dateTimePattern)
        )
    }

    fun getPresentationDate(): String {
        return startTime.toStr(datePresentationPattern)
    }

    fun getPresentationTime(): String {
        return "${startTime.toStr(timePresentationPattern)} - ${endTime.toStr(timePresentationPattern)}"
    }
}
