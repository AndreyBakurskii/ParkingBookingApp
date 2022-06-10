package com.example.parking.presentation.fragments.reservation.create.admin.elm

import com.example.parking.data.models.Car
import com.example.parking.data.models.Employee
import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.models.Reservation
import com.example.parking.data.network.modelJSON.ReservationJson
import java.util.*
import kotlin.collections.ArrayList

data class State(
    val loading: Boolean = false,
    val employee: Employee? = null,
    val carModel: String = "", val carRegistryNumber: String = "",
    val startTime: Date? = null, val endTime: Date? = null,

    val reservedCar: Car? = null,
    val reservedParkingSpot: ParkingSpot? = null,

    val parkingSpots: ArrayList<ParkingSpot> = arrayListOf(),
    val reservations: ArrayList<ReservationJson> = arrayListOf(),
)

sealed class Effect {
    data class ShowConfirmDialog(
        val carModel: String, val carRegistryNumber: String,
        val employee: Employee,
        val startTime: Date, val endTime: Date,
    ) : Effect()
    object ShowErrorNotFoundCar : Effect()
    object ShowErrorNotFoundEmployee : Effect()
    object ShowErrorNotFreeParkingSpots : Effect()

    object ShowErrorCreateReservation : Effect()

    object ShowErrorNetwork : Effect()

    object ToReservationsFragment : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class CreateClick(
            val carModel: String, val carRegistryNumber: String,
            val employee: Employee,
            val startTime: Date, val endTime: Date,
        ) : Ui()
        data class OkClickConfirmDialog(
            val carModel: String, val carRegistryNumber: String,
            val employee: Employee,
            val startTime: Date, val endTime: Date,
        ) : Ui()
    }

    sealed class Internal : Event() {
        object SuccessCreateReservation : Internal()
        object ErrorCreateReservation : Internal()

        data class SuccessCheckCar(val car: Car) : Internal()
        object ErrorNotFoundCar : Internal()

        object SuccessCheckEmployee : Internal()
        object ErrorNotFoundEmployee : Internal()

        data class SuccessLoadAllReservations(val reservations: ArrayList<ReservationJson>) : Internal()

        data class SuccessLoadAllParkingSpots(val parkingSpots: ArrayList<ParkingSpot>) : Internal()

        data class SuccessGetFreeParkingSpots(val parkingSpot: ParkingSpot) : Internal()
        object ErrorNotFreeParkingSpots : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class CheckExistCar(val carModel: String, val carRegistryNumber: String) : Command()
    data class CheckExistEmployee(val employee: Employee) : Command()
    object LoadAllReservations : Command()
    object LoadAllParkingSpots : Command()
    data class GetFreeParkingSpots(
        val reservations: ArrayList<ReservationJson>,
        val parkingSpots: ArrayList<ParkingSpot>,
        val startTime: Date, val endTime: Date
    ) : Command()
    data class CreateReservation(
        val employee: Employee,
        val car: Car,
        val parkingSpot: ParkingSpot,
        val startTime: Date, val endTime: Date
    ) : Command()
}
