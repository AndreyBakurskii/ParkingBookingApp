package com.example.parking.data.models;

import com.example.parking.utils.toStr
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class Reservation (
    val id: UUID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
    var car: Car,
    var employee: Employee,
    var parkingSpot: ParkingSpot,
    var startTime: Date,
    var endTime: Date
) {
    private val dateTimePattern: String = "yyyy-MM-dd'T'HH:mm:ss"
    private val datePresentationPattern: String = "EEE dd/MM"
    private val timePresentationPattern: String = "HH:mm"
    private val serverTimeZone: TimeZone = TimeZone.getTimeZone("GMT")
    private val localTimeZone: TimeZone = TimeZone.getDefault()

    fun toHashMap(withID: Boolean = false) : HashMap<String, String> {
        return if (withID) hashMapOf(
            "id" to id.toString(),
            "carId" to car.id.toString(),
            "employeeId" to employee.id.toString(),
            "parkingSpot" to parkingSpot.id.toString(),
            "startTime" to startTime.toStr(dateTimePattern, serverTimeZone),
            "endTime" to endTime.toStr(dateTimePattern, serverTimeZone)
        ) else hashMapOf(
            "carId" to car.id.toString(),
            "employeeId" to employee.id.toString(),
            "parkingSpot" to parkingSpot.id.toString(),
            "startTime" to startTime.toStr(dateTimePattern, serverTimeZone),
            "endTime" to endTime.toStr(dateTimePattern, serverTimeZone)
        )
    }

    fun getPresentationDate(): String {
        return startTime.toStr(datePresentationPattern, localTimeZone)
    }

    fun getPresentationTime(): String {
        return "${startTime.toStr(timePresentationPattern, localTimeZone)} -" +
                " ${endTime.toStr(timePresentationPattern, localTimeZone)}"
    }
}
