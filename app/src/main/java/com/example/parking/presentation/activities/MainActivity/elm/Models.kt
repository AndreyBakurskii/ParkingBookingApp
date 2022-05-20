package com.example.parking.presentation.activities.MainActivity.elm

import com.example.parking.data.network.modelJSON.CarJson

data class State(
//    val alertDialogEmail: Boolean = false,
//    val alertDialogPassword: Boolean = false
    val pass: Boolean = false
)

sealed class Effect {
    object ShowAlertDialogEmail : Effect()
    object ShowErrorInvalidEmail : Effect()
    object ShowAlertDialogPassword : Effect()
    object ShowErrorInvalidPassword : Effect()
    object ToUserMainActivity : Effect()
    object ToAdminMainActivity : Effect()
    data class ShowCars(var cars: List<CarJson>) : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class StartClick(var selectedRole: Int) : Ui()
        data class OkClickAlertDialogEmail(var email: String) : Ui()
        data class OkClickAlertDialogPassword(var password: String) : Ui()
    }

    sealed class Internal : Event() {
//        data class CheckAdminPassword(val password: String) : Internal()
        object SuccessCheckAdminPassword  : Internal()
        object ErrorInvalidPassword : Internal()
        object SuccessCheckUserEmail  : Internal()
        object ErrorInvalidUserEmail : Internal()
        data class SuccessLoadAllCars(var cars: List<CarJson>) : Internal()
    }
}

sealed class Command {
    object getAllCars : Command()
}

