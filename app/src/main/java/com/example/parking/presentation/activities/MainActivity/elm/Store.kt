package com.example.parking.presentation.activities.MainActivity.elm

import com.example.parking.data.models.createEmployee
import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.EmployeeRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.presentation.activities.MainActivity.elm.Event.Internal
import com.example.parking.presentation.activities.MainActivity.elm.Event.Ui
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.NoOpActor
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.util.*


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
            effects { +Effect.ToUserMainActivity(event.email) }
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
                commands { +Command.CheckExistEmployee(event.email) }
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

class MyActor : Actor<Command, Event> {
    private val employeeRepository: EmployeeRepository = EmployeeRepository(
        NetworkService("user", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.CheckExistEmployee -> employeeRepository
            .getEmployee(createEmployee(command.email).id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessCheckUserEmail(command.email) },
                    errorHandler = { Internal.ErrorInvalidUserEmail }
                )},
                errorMapper = { Internal.ErrorInvalidUserEmail }
            )
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor(),
)