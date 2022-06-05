package com.example.parking.presentation.fragments.parkingSpot.create.elm

import com.example.parking.data.models.ParkingSpot

data class State(
    val loading: Boolean = false
)

sealed class Effect {
    data class ShowConfirmDialog(var parkingSpot: ParkingSpot) : Effect()
    object ShowErrorCreateParkingSpot : Effect()
    object ShowErrorNetwork : Effect()
    object ToParkingSpotsFragment : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class CreateClick(var parkingSpot: ParkingSpot) : Ui()
        data class OkClickConfirmDialog(var parkingSpot: ParkingSpot) : Ui()
    }

    sealed class Internal : Event() {
        object SuccessCreateParkingSpot : Internal()
        object ErrorCreateParkingSpot : Internal()
        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class CreateParkingSpot(var parkingSpot: ParkingSpot) : Command()
}