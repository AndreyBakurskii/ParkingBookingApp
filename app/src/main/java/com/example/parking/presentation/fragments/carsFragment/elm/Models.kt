package com.example.parking.presentation.fragments.carsFragment.elm

import com.example.parking.data.models.Car


data class State(
    val loading: Boolean = false,
    val cars: ArrayList<Car> = arrayListOf(),
    val doUpdate: Boolean = false
)

sealed class Effect {
    object ShowErrorLoadCars : Effect()
    object ShowErrorDeleteCar : Effect()
    object ShowErrorNetwork : Effect()
    object ToCreateCarFragment : Effect()
    data class ToEditCarFragment(var car: Car, var positionInAdapter: Int) : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        object LoadCars : Ui()
        data class ClickEditCar(var car: Car, var positionInAdapter: Int) : Ui()
        data class ClickDeleteCar(var car: Car, var positionInAdapter: Int) : Ui()
        object ClickCreateCar : Ui()
    }

    sealed class Internal : Event() {
        data class SuccessLoadCars(var cars: ArrayList<Car>) : Internal()
        object ErrorLoadCars : Internal()

        data class SuccessDeleteFromServer(val car: Car, val positionInAdapter: Int) : Internal()
        data class SuccessDeleteFromAdapter(val cars: ArrayList<Car>) : Internal()
        object ErrorDeleteCar : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    object LoadAllCar : Command()
    data class DeleteFromServer(var car: Car, var positionInAdapter: Int) : Command()
    data class DeleteFromAdapter(var cars: ArrayList<Car>, var positionInAdapter: Int) : Command()
}