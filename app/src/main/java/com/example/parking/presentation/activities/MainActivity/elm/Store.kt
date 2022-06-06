package com.example.parking.presentation.activities.MainActivity.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.presentation.activities.MainActivity.elm.Event.Internal
import com.example.parking.presentation.activities.MainActivity.elm.Event.Ui
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.NoOpActor
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer


class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessCheckAdminPassword -> {
            state { copy(pass = true) }
            effects { +Effect.ToAdminMainActivity }
        }
        is Internal.ErrorInvalidPassword -> {
            state { copy(pass = false) }
            effects { +Effect.ShowErrorInvalidPassword }
        }
        is Internal.SuccessCheckUserEmail -> {
            state { copy(pass = true) }
            effects { +Effect.ToUserMainActivity }
        }
        is Internal.ErrorInvalidUserEmail -> {
            state { copy(pass = false) }
            effects { +Effect.ShowErrorInvalidEmail }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> { }
        is Ui.StartClick -> {
            state { copy(pass = false) }
            if (event.selectedRole == 0) {
                effects { +Effect.ShowAlertDialogEmail }
            } else {
                effects { +Effect.ShowAlertDialogPassword }
            }
        }
        is Ui.OkClickAlertDialogEmail -> {
            state { copy(pass = false) }
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) {
                effects { +Effect.ToUserMainActivity }
            } else {
                effects { +Effect.ShowErrorInvalidEmail }
            }
        }
        is Ui.OkClickAlertDialogPassword -> {
            state { copy(pass = false) }
            if (event.password == "admin") {
                effects { +Effect.ToAdminMainActivity }
            } else {
                effects { +Effect.ShowErrorInvalidPassword }
            }
        }
    }
}

fun storeFactory() = ElmStore(
    initialState = State(),
    reducer = Reducer(),
    actor = NoOpActor(),
)