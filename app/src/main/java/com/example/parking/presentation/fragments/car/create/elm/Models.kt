package com.example.parking.presentation.fragments.car.create.elm

import com.example.parking.data.models.Car


data class State(
    val loading: Boolean = false
)

sealed class Effect {
    data class ShowConfirmDialog(var car: Car) : Effect()
    object ShowErrorCreateCar : Effect()
    object ShowErrorNetwork : Effect()
    object ToCarsFragment : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class CreateClick(var car: Car) : Ui()
        data class OkClickConfirmDialog(var car: Car) : Ui()
    }

    sealed class Internal : Event() {
        object SuccessCreateCar : Internal()
        object ErrorCreateCar : Internal()
        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class CreateCar(var car: Car) : Command()
}

