package com.example.parking.presentation.fragments.carsFragment.elm

import com.example.parking.data.models.Car


data class State(
    val loading: Boolean = false,
    val cars: List<Car>? = null,
    val doUpdate: Boolean = false
)

sealed class Effect {
    object ShowErrorLoadCars : Effect()
    object ShowErrorDeleteCar : Effect()
    object ShowErrorNetwork : Effect()
//    object ToEditCar : Effect() todo обработать!
}

sealed class Event {
    sealed class Ui : Event() {
        object LoadCars : Ui()
        data class ClickEditCar(var car: Car, var positionInAdapter: Int) : Ui()
        data class ClickDeleteCar(var car: Car, var positionInAdapter: Int) : Ui()
    }

    sealed class Internal : Event() {
        data class SuccessLoadCars(var cars: List<Car>) : Internal()
        object ErrorLoadCars : Internal()

//        object SuccessEditCar : Internal()
//        object ErrorEditCar : Internal()

        object SuccessDeleteCar : Internal()
        object ErrorDeleteCar : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    object LoadAllCar : Command()
    data class DeleteCar(var car: Car, var positionInAdapter: Int) : Command()
//    data class EditCar(var car: Car) : Command()
}