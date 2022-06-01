package com.example.parking.presentation.fragments.edit.carFragment.elm

import com.example.parking.data.models.Car

data class State(
    val loading: Boolean = false
)

sealed class Effect {
    data class ShowConfirmDialog(var car: Car) : Effect()
    object ShowErrorEditCar : Effect()
    object ShowErrorNetwork : Effect()
    object ToCarsFragment : Effect()
}

sealed class Event {
    sealed class Ui: Event() {
        object Init : Ui()
        data class EditClick(var car: Car) : Ui()
        data class OkClickConfirmDialog(var car: Car) : Ui()
    }
    sealed class Internal: Event() {
        object SuccessEditCar : Internal()
        object ErrorEditCar : Internal()
        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class EditCar(var car: Car) : Command()
}
