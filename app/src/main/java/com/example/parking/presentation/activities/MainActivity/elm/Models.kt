package com.example.parking.presentation.activities.MainActivity.elm

import com.example.parking.data.models.Employee
import com.example.parking.data.network.modelJSON.CarJson

data class State(
    val pass: Boolean = false
)

sealed class Effect {
    object ShowAlertDialogEmail : Effect()
    object ShowErrorInvalidEmail : Effect()
    object ShowAlertDialogPassword : Effect()
    object ShowErrorInvalidPassword : Effect()
    data class ToUserMainActivity(val email: String) : Effect()
    object ToAdminMainActivity : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class StartClick(var selectedRole: Int) : Ui()
        data class OkClickAlertDialogEmail(var email: String) : Ui()
        data class OkClickAlertDialogPassword(var password: String) : Ui()
    }

    sealed class Internal : Event() {
        object SuccessCheckAdminPassword  : Internal()
        object ErrorInvalidPassword : Internal()
        data class SuccessCheckUserEmail(val email: String)  : Internal()
        object ErrorInvalidUserEmail : Internal()
    }
}

sealed class Command {
    data class CheckExistEmployee(val email: String) : Command()
}

