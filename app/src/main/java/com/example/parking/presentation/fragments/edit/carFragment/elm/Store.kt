package com.example.parking.presentation.fragments.edit.carFragment.elm

import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.CarRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.presentation.fragments.edit.carFragment.elm.Event.Ui
import com.example.parking.presentation.fragments.edit.carFragment.elm.Event.Internal
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat

class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessEditCar -> {
            state { copy(loading = false) }
            effects { +Effect.ToCarsFragment }
        }
        is Internal.ErrorEditCar -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorEditCar }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {}
        is Ui.EditClick -> {
            effects { +Effect.ShowConfirmDialog(event.car) }
        }
        is Ui.OkClickConfirmDialog -> {
            state { copy(loading = true) }
            commands { +Command.EditCar(event.car) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val carRepository: CarRepository = CarRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.EditCar -> carRepository
            .updateCar(command.car.id.toString(), command.car.toHashMap(withID = false))
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessEditCar },
                    errorHandler = { Internal.ErrorEditCar }
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