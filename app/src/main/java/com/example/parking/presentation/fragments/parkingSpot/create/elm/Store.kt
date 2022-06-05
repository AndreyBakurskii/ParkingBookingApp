package com.example.parking.presentation.fragments.parkingSpot.create.elm

import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.ParkingSpotRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

import com.example.parking.presentation.fragments.parkingSpot.create.elm.Event.Ui
import com.example.parking.presentation.fragments.parkingSpot.create.elm.Event.Internal
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat


class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessCreateParkingSpot -> {
            state { copy(loading = false) }
            effects { +Effect.ToParkingSpotsFragment }
        }
        is Internal.ErrorCreateParkingSpot -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorCreateParkingSpot }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {}
        is Ui.CreateClick -> {
            effects { +Effect.ShowConfirmDialog(event.parkingSpot) }
        }
        is Ui.OkClickConfirmDialog -> {
            state { copy(loading = true) }
            commands { +Command.CreateParkingSpot(event.parkingSpot) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val parkingSpotRepository: ParkingSpotRepository = ParkingSpotRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.CreateParkingSpot -> parkingSpotRepository
            .createParkingSpot(command.parkingSpot.toHashMap(withID = false))
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessCreateParkingSpot },
                    errorHandler = { Internal.ErrorCreateParkingSpot }
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