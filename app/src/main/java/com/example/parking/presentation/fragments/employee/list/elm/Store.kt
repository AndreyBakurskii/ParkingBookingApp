package com.example.parking.presentation.fragments.employee.list.elm

import com.example.parking.data.mapper.EmployeeMapper
import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.EmployeeRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.presentation.fragments.employee.list.elm.Event.Ui
import com.example.parking.presentation.fragments.employee.list.elm.Event.Internal
import com.example.parking.presentation.utils.removeElementByIndex
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat


class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessLoadEmployees -> {
            state { copy(
                loading = false,
                employees = event.employees,
                doUpdate = true
            ) }
        }
        is Internal.ErrorLoadEmployees -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorLoadEmployees }
        }
        is Internal.SuccessDeleteFromServer -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.DeleteFromAdapter(state.employees, event.positionInAdapter) }
        }
        is Internal.SuccessDeleteFromAdapter -> {
            state { copy(loading = false, employees = event.employees, doUpdate = true) }
        }
        is Internal.ErrorDeleteEmployee -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorDeleteEmployee }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {
        }
        is Ui.LoadEmployees -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.LoadAllEmployee }
        }
        is Ui.ClickDeleteEmployee -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowDeleteDialog(event.employee, event.positionInAdapter) }
        }
        is Ui.ClickCreateEmployee -> {
            state {copy(loading = false, doUpdate = false)}
            effects { +Effect.ToCreateEmployeeFragment }
        }
        is Ui.OkClickDeleteDialog -> {
            state {copy(loading = true, doUpdate = false)}
            commands { +Command.DeleteFromServer(event.employee, event.positionInAdapter) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val employeeMapper: EmployeeMapper = EmployeeMapper()
    private val employeeRepository: EmployeeRepository = EmployeeRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadAllEmployee -> employeeRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadEmployees(
                        employees = ArrayList(it
                            .map { employeeJson -> employeeMapper.fromJsonToModel(employeeJson) }
                            .sortedBy { employee -> employee.name }
                        )
                    ) },
                    errorHandler = { Internal.ErrorLoadEmployees }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteFromServer -> employeeRepository
            .deleteEmployee(id = command.employee.id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessDeleteFromServer(command.employee, command.positionInAdapter) },
                    errorHandler = { Internal.ErrorDeleteEmployee }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteFromAdapter -> Observable
            .fromArray(command.employees.removeElementByIndex(command.positionInAdapter))
            .mapEvents(
                eventMapper = { Internal.SuccessDeleteFromAdapter(employees = it) },
                errorMapper = { Internal.ErrorDeleteEmployee}
            )
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor()
)