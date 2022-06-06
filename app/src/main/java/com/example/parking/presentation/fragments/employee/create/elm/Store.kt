package com.example.parking.presentation.fragments.employee.create.elm

import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.EmployeeRepository
import com.example.parking.presentation.fragments.employee.create.elm.Event.Ui
import com.example.parking.presentation.fragments.employee.create.elm.Event.Internal
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer


class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessCreateEmployee -> {
            state { copy(loading = false) }
            effects { +Effect.ToEmployeesFragment }
        }
        is Internal.ErrorCreateEmployee -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorCreateEmployee }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNetwork }
        }
        is Internal.SuccessCheckExistEmployee -> {
            state { copy(loading = true) }
            commands { +Command.CreateEmployee(event.employee) }
        }
        is Internal.ErrorCheckExistEmployee -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorEmployeeExist }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {}
        is Ui.CreateClick -> {
            effects { +Effect.ShowConfirmDialog(event.employee) }
        }
        is Ui.OkClickConfirmDialog -> {
            state { copy(loading = true) }
            commands { +Command.CheckExistEmployee(event.employee) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val employeeRepository: EmployeeRepository = EmployeeRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.CheckExistEmployee -> employeeRepository
            .getEmployee(command.employee.id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.ErrorCheckExistEmployee },
                    errorHandler = { Internal.SuccessCheckExistEmployee(command.employee) }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.CreateEmployee -> employeeRepository
            .createEmployee(command.employee.toHashMap(withID = true))
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessCreateEmployee },
                    errorHandler = { Internal.ErrorCreateEmployee }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor()
)