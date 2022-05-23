package com.example.parking.presentation.activities.MainActivity.elm

import com.example.parking.data.network.modelJSON.CarJson

data class State(
    val pass: Boolean = false
)

sealed class Effect {
    object ShowAlertDialogEmail : Effect()
    object ShowErrorInvalidEmail : Effect()
    object ShowAlertDialogPassword : Effect()
    object ShowErrorInvalidPassword : Effect()
    object ToUserMainActivity : Effect()
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
        object SuccessCheckUserEmail  : Internal()
        object ErrorInvalidUserEmail : Internal()
    }
}

sealed class Command {
}

