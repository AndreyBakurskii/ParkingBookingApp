package com.example.parking.presentation.fragments.car.list.elm

import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.models.Car
import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.CarRepository
import com.example.parking.presentation.fragments.car.list.elm.Event.Internal
import com.example.parking.presentation.fragments.car.list.elm.Event.Ui
import com.example.parking.presentation.utils.removeElementByIndex
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer


class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessLoadCars -> {
            state { copy(
                loading = false,
                cars = event.cars,
                doUpdate = true
            ) }
        }
        is Internal.ErrorLoadCars -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorLoadCars }
        }
        is Internal.SuccessDeleteFromServer -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.DeleteFromAdapter(state.cars, event.positionInAdapter) }
        }
        is Internal.SuccessDeleteFromAdapter -> {
            state { copy(loading = false, cars = event.cars, doUpdate = true) }
        }
        is Internal.ErrorDeleteCar -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorDeleteCar }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {
        }
        is Ui.LoadCars -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.LoadAllCar }
        }
        is Ui.ClickDeleteCar -> {
            state { copy(loading = true, doUpdate = false) }
            effects { +Effect.ShowDeleteDialog(event.car, event.positionInAdapter) }
        }
        is Ui.ClickEditCar -> {
            state {copy(loading = false, doUpdate = false)}
            effects { +Effect.ToEditCarFragment(event.car, event.positionInAdapter) }
        }
        is Ui.ClickCreateCar -> {
            state {copy(loading = false, doUpdate = false)}
            effects { +Effect.ToCreateCarFragment }
        }
        is Ui.OkClickDeleteDialog -> {
            state {copy(loading = true, doUpdate = false)}
            commands { +Command.DeleteFromServer(event.car, event.positionInAdapter) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val carMapper: CarMapper = CarMapper()
    private val carRepository: CarRepository = CarRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadAllCar -> carRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadCars(
                        cars = ArrayList(it
                            .map { carJson -> carMapper.fromJsonToModel(carJson) }
                            .sortedBy { car -> car.model }
                        )
                    ) },
                    errorHandler = { Internal.ErrorLoadCars }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteFromServer -> carRepository
            .deleteCar(id = command.car.id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessDeleteFromServer(command.car, command.positionInAdapter) },
                    errorHandler = { Internal.ErrorDeleteCar }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteFromAdapter -> Observable
            .fromArray(command.cars.removeElementByIndex(command.positionInAdapter))
            .mapEvents(
                eventMapper = { Internal.SuccessDeleteFromAdapter(cars = it) },
                errorMapper = { Internal.ErrorDeleteCar}
            )
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor()
)