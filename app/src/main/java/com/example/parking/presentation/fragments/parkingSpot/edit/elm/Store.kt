package com.example.parking.presentation.fragments.parkingSpot.edit.elm

import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.ParkingSpotRepository
import com.example.parking.presentation.fragments.parkingSpot.edit.elm.Event.Ui
import com.example.parking.presentation.fragments.parkingSpot.edit.elm.Event.Internal
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessEditParkingSpot -> {
            state { copy(loading = false) }
            effects { +Effect.ToParkingSpotsFragment }
        }
        is Internal.ErrorEditParkingSpot -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorEditParkingSpot }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }

    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {}
        is Ui.EditClick -> {
            effects { +Effect.ShowConfirmDialog(event.parkingSpot) }
        }
        is Ui.OkClickConfirmDialog -> {
            state { copy(loading = true) }
            commands { +Command.EditParkingSpot(event.parkingSpot) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val parkingSpotRepository: ParkingSpotRepository = ParkingSpotRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.EditParkingSpot -> parkingSpotRepository
            .updateParkingSpot(command.parkingSpot.id.toString(), command.parkingSpot.toHashMap(withID = false))
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessEditParkingSpot },
                    errorHandler = { Internal.ErrorEditParkingSpot }
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