package com.example.parking.ELMMainActivity

import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.ELMMainActivity.Event.Internal
import com.example.parking.ELMMainActivity.Event.Ui
import com.example.parking.api.CarAPI
import com.example.parking.api.NetworkService
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.NoOpActor


class MyActor : Actor<Command, Internal> {

    override fun execute(command: Command): Observable<Internal> = when (command) {
        is Command.getAllCars -> CarAPI(
            NetworkService("admin", "password").retrofit
        )
            .getAll()
            .mapEvents(
                eventMapper = { Internal.SuccessLoadAllCars(cars = it) },
                errorMapper = { Internal.ErrorInvalidUserEmail }
            )
    }
}

class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
    Ui::class, Internal::class) {

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
        is Internal.SuccessLoadAllCars -> {
            state { copy(pass = false) }
            effects { +Effect.ShowCars(cars = event.cars) }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {
            state { copy(pass = false) }
            commands { +Command.getAllCars }
        }
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

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor(),
)