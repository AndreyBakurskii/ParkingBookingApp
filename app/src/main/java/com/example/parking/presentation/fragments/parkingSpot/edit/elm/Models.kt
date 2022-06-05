package com.example.parking.presentation.fragments.parkingSpot.edit.elm

import com.example.parking.data.models.ParkingSpot

data class State(
    val loading: Boolean = false
)

sealed class Effect {
    data class ShowConfirmDialog(var parkingSpot: ParkingSpot) : Effect()
    object ShowErrorEditParkingSpot : Effect()
    object ShowErrorNetwork : Effect()
    object ToParkingSpotsFragment : Effect()
}

sealed class Event {
    sealed class Ui: Event() {
        object Init : Ui()
        data class EditClick(var parkingSpot: ParkingSpot) : Ui()
        data class OkClickConfirmDialog(var parkingSpot: ParkingSpot) : Ui()
    }
    sealed class Internal: Event() {
        object SuccessEditParkingSpot : Internal()
        object ErrorEditParkingSpot : Internal()
        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class EditParkingSpot(var parkingSpot: ParkingSpot) : Command()
}