package com.example.parking.presentation.fragments.parkingSpot.list.elm

import com.example.parking.data.models.ParkingSpot

data class State(
    val loading: Boolean = false,
    val parkingSpots: ArrayList<ParkingSpot> = arrayListOf(),
    val doUpdate: Boolean = false
)

sealed class Effect {
    object ShowErrorLoadParkingSpots : Effect()
    object ShowErrorDeleteParkingSpot : Effect()
    object ShowErrorNetwork : Effect()
    object ToCreateParkingSpotFragment : Effect()
    data class ShowDeleteDialog(var parkingSpot: ParkingSpot, var positionInAdapter: Int) : Effect()
    data class ToEditParkingSpotFragment(var parkingSpot: ParkingSpot, var positionInAdapter: Int) : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        object LoadParkingSpots : Ui()
        data class ClickEditParkingSpot(var parkingSpot: ParkingSpot, var positionInAdapter: Int) : Ui()
        data class ClickDeleteParkingSpot(var parkingSpot: ParkingSpot, var positionInAdapter: Int) : Ui()
        data class OkClickDeleteDialog(var parkingSpot: ParkingSpot, var positionInAdapter: Int) : Ui()
        object ClickCreateParkingSpot : Ui()
    }

    sealed class Internal : Event() {
        data class SuccessLoadParkingSpots(var parkingSpots: ArrayList<ParkingSpot>) : Internal()
        object ErrorLoadParkingSpots : Internal()

        data class SuccessDeleteFromServer(val parkingSpot: ParkingSpot, val positionInAdapter: Int) : Internal()
        data class SuccessDeleteFromAdapter(val parkingSpots: ArrayList<ParkingSpot>) : Internal()
        object ErrorDeleteParkingSpot : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    object LoadAllParkingSpot : Command()
    data class DeleteFromServer(var parkingSpot: ParkingSpot, var positionInAdapter: Int) : Command()
    data class DeleteFromAdapter(var parkingSpots: ArrayList<ParkingSpot>, var positionInAdapter: Int) : Command()
}