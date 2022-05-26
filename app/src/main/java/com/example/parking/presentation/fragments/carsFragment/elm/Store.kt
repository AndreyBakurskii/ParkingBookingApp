package com.example.parking.presentation.fragments.carsFragment.elm

import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.CarRepository
import com.example.parking.presentation.fragments.carsFragment.elm.Event.Internal
import com.example.parking.presentation.fragments.carsFragment.elm.Event.Ui
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
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
        is Internal.SuccessDeleteCar -> {
            state { copy(loading = false, doUpdate = true) }  // todo решить что будет с cars
        }
        is Internal.ErrorDeleteCar -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorDeleteCar }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorDeleteCar }
        }
    }
    override fun Result.ui(event: Ui) = when (event) {
        is Ui.LoadCars -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.LoadAllCar }
        }
        is Ui.ClickDeleteCar -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.DeleteCar(event.car, event.positionInAdapter) }
        }
        is Ui.ClickEditCar -> {
            // todo обработать!
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
                        cars = it.map { carJson -> carMapper.fromJsonToModel(carJson) }
                    ) },
                    errorHandler = { Internal.ErrorLoadCars }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteCar -> carRepository
            .deleteCar(id = command.car.id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessDeleteCar },
                    errorHandler = { Internal.ErrorDeleteCar }
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