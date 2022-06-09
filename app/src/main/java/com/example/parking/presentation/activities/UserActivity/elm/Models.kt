package com.example.parking.presentation.activities.UserActivity.elm

import com.example.parking.data.models.Car
import com.example.parking.data.models.Employee
import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.models.Reservation
import com.example.parking.data.network.modelJSON.ReservationJson

data class State(
    val loading: Boolean = false,
    val doUpdate: Boolean = false,

    val employee: Employee? = null,
    val reservationsJson: ArrayList<ReservationJson> = arrayListOf(),
    val reservations: ArrayList<Reservation> = arrayListOf(),
    val cars: ArrayList<Car> = arrayListOf(),
    val parkingSpots: ArrayList<ParkingSpot> = arrayListOf()
)

sealed class Effect {
    object ShowErrorLoadReservations : Effect()
//    object ShowErrorDeleteReservation : Effect()
    object ShowErrorNetwork : Effect()

//    data class ShowDeleteDialog(var reservation: Reservation, var positionInAdapter: Int) : Effect()

    object ToCreateReservationFragment : Effect()
//    data class ToEditReservationFragment(val reservation: Reservation) : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class LoadReservations(var employee: Employee) : Ui()
//        data class ClickEditReservation(var reservation: Reservation, var positionInAdapter: Int) : Ui()
//        data class ClickDeleteReservation(var reservation: Reservation, var positionInAdapter: Int) : Ui()
//        data class OkClickDeleteDialog(var reservation: Reservation, var positionInAdapter: Int) : Ui()
        object ClickCreateReservation : Ui()
    }

    sealed class Internal : Event() {
        data class SuccessLoadReservationsJson(var reservationsJson: ArrayList<ReservationJson>) : Internal()
        object ErrorLoadReservations : Internal()

        data class SuccessLoadCars(var cars: ArrayList<Car>) : Internal()
        data class SuccessLoadParkingSpots(var parkingSpots: ArrayList<ParkingSpot>) : Internal()

        data class SuccessInitReservations(var reservations: ArrayList<Reservation>) : Internal()

//        data class SuccessDeleteFromServer(var reservation: Reservation, var positionInAdapter: Int) : Internal()
//        data class SuccessDeleteFromAdapter(var reservations: ArrayList<Reservation>) : Internal()
//        object ErrorDeleteReservation : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    object LoadAllReservations : Command()
    object LoadAllCars : Command()
    object LoadAllParkingSpots : Command()

    data class InitReservations(
        var reservationsJson: ArrayList<ReservationJson>,
        var cars: ArrayList<Car>,
        var employee: Employee,
        var parkingSpots: ArrayList<ParkingSpot>
    ) : Command()

//    data class DeleteFromServer(var reservation: Reservation, var positionInAdapter: Int) : Command()
//    data class DeleteFromAdapter(var reservations: ArrayList<Reservation>, var positionInAdapter: Int) : Command()
}