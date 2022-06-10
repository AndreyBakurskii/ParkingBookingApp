package com.example.parking.presentation.fragments.reservation.edit.admin.elm

import com.example.parking.data.models.Car
import com.example.parking.data.models.Employee
import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.models.Reservation
import com.example.parking.data.network.modelJSON.ReservationJson
import java.util.*
import kotlin.collections.ArrayList

data class State(
    val loading: Boolean = false,
    val reservation: Reservation? = null,

    val updatedEmployee: Employee? = null,
    val updatedCarModel: String = "",
    val updatedCarRegistryNumber: String = "",
    val updatedStartTime: Date? = null,
    val updatedEndTime: Date? = null,

    val reservedCar: Car? = null,
    val reservedParkingSpot: ParkingSpot? = null,

    val parkingSpots: ArrayList<ParkingSpot> = arrayListOf(),
    val reservations: ArrayList<ReservationJson> = arrayListOf(),
)

sealed class Effect {
    data class ShowConfirmDialog(
        val reservation: Reservation,
        val updatedCarModel: String, val updatedCarRegistryNumber: String,
        val updatedEmployee: Employee,
        val updatedStartTime: Date, val updatedEndTime: Date,
    ) : Effect()
    object ShowErrorNotFoundCar : Effect()
    object ShowErrorNotFoundEmployee : Effect()
    object ShowErrorNotFreeParkingSpots : Effect()

    object ShowErrorUpdateReservation : Effect()

    object ShowErrorNetwork : Effect()

    object ToReservationsFragment : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class UpdateClick(
            val reservation: Reservation,
            val updatedCarModel: String, val updatedCarRegistryNumber: String,
            val updatedEmployee: Employee,
            val updatedStartTime: Date, val updatedEndTime: Date,
        ) : Ui()
        data class OkClickConfirmDialog(
            val reservation: Reservation,
            val updatedCarModel: String, val updatedCarRegistryNumber: String,
            val updatedEmployee: Employee,
            val updatedStartTime: Date, val updatedEndTime: Date,
        ) : Ui()
    }

    sealed class Internal : Event() {
        object SuccessUpdateReservation : Internal()
        object ErrorUpdateReservation : Internal()

        data class SuccessCheckCar(val car: Car) : Internal()
        object ErrorNotFoundCar : Internal()

        object SuccessCheckEmployee : Internal()
        object ErrorNotFoundEmployee : Internal()

        object NeedNewParkingSpot : Internal()
        data class NoNeedNewParkingSpot(val currentParkingSpot: ParkingSpot) : Internal()

        data class SuccessLoadAllReservations(val reservations: ArrayList<ReservationJson>) : Internal()

        data class SuccessLoadAllParkingSpots(val parkingSpots: ArrayList<ParkingSpot>) : Internal()

        data class SuccessGetFreeParkingSpots(val parkingSpot: ParkingSpot) : Internal()
        object ErrorNotFreeParkingSpots : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class CheckExistCar(
        val currentCar: Car,
        val updatedCarModel: String,
        val updatedCarRegistryNumber: String
    ) : Command()

    data class CheckExistEmployee(
        val currentEmployee: Employee,
        val updatedEmployee: Employee
    ) : Command()

    data class CheckNeedNewParkingSpot(
        val currentParkingSpot: ParkingSpot,
        val currentStartTime: Date,
        val currentEndTime: Date,
        val updatedStartTime: Date,
        val updatedEndTime: Date
    ) : Command()

    object LoadAllReservations : Command()

    object LoadAllParkingSpots : Command()

    data class GetFreeParkingSpots(
        val reservation: Reservation,
        val reservations: ArrayList<ReservationJson>,
        val parkingSpots: ArrayList<ParkingSpot>,
        val updatedStartTime: Date, val updatedEndTime: Date
    ) : Command()

    data class UpdateReservation(
        val reservationId: String,
        val updatedEmployee: Employee,
        val reservedCar: Car,
        val reservedParkingSpot: ParkingSpot,
        val updatedStartTime: Date, val updatedEndTime: Date
    ) : Command()
}