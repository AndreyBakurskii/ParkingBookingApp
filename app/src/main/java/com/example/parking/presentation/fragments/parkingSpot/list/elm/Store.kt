package com.example.parking.presentation.fragments.parkingSpot.list.elm

import com.example.parking.data.mapper.ParkingSpotMapper
import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.ParkingSpotRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

import com.example.parking.presentation.fragments.parkingSpot.list.elm.Event.Ui
import com.example.parking.presentation.fragments.parkingSpot.list.elm.Event.Internal
import com.example.parking.presentation.utils.removeElementByIndex
import com.example.parking.presentation.utils.statusCodeHandler
import io.reactivex.Observable
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat


class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessLoadParkingSpots -> {
            state { copy(
                loading = false,
                parkingSpots = event.parkingSpots,
                doUpdate = true
            ) }
        }
        is Internal.ErrorLoadParkingSpots -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorLoadParkingSpots }
        }
        is Internal.SuccessDeleteFromServer -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.DeleteFromAdapter(state.parkingSpots, event.positionInAdapter) }
        }
        is Internal.SuccessDeleteFromAdapter -> {
            state { copy(loading = false, parkingSpots = event.parkingSpots, doUpdate = true) }
        }
        is Internal.ErrorDeleteParkingSpot -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorDeleteParkingSpot }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }
    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {
        }
        is Ui.LoadParkingSpots -> {
            state { copy(loading = true, doUpdate = false) }
            commands { +Command.LoadAllParkingSpot }
        }
        is Ui.ClickDeleteParkingSpot -> {
            state { copy(loading = true, doUpdate = false) }
            effects { +Effect.ShowDeleteDialog(event.parkingSpot, event.positionInAdapter) }
        }
        is Ui.ClickEditParkingSpot -> {
            state {copy(loading = false, doUpdate = false)}
            effects { +Effect.ToEditParkingSpotFragment(event.parkingSpot, event.positionInAdapter) }
        }
        is Ui.ClickCreateParkingSpot -> {
            state {copy(loading = false, doUpdate = false)}
            effects { +Effect.ToCreateParkingSpotFragment }
        }
        is Ui.OkClickDeleteDialog -> {
            state {copy(loading = true, doUpdate = false)}
            commands { +Command.DeleteFromServer(event.parkingSpot, event.positionInAdapter) }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val parkingSpotMapper: ParkingSpotMapper = ParkingSpotMapper()
    private val parkingSpotRepository: ParkingSpotRepository = ParkingSpotRepository(
        NetworkService("admin", "password").retrofit
    )

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadAllParkingSpot -> parkingSpotRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadParkingSpots(
                        parkingSpots = ArrayList(it
                            .map { parkingSpotJson -> parkingSpotMapper.fromJsonToModel(parkingSpotJson) }
                            .sortedBy { parkingSpot -> parkingSpot.parkingNumber }
                        )
                    ) },
                    errorHandler = { Internal.ErrorLoadParkingSpots }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteFromServer -> parkingSpotRepository
            .deleteParkingSpot(id = command.parkingSpot.id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessDeleteFromServer(command.parkingSpot, command.positionInAdapter) },
                    errorHandler = { Internal.ErrorDeleteParkingSpot }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.DeleteFromAdapter -> Observable
            .fromArray(command.parkingSpots.removeElementByIndex(command.positionInAdapter))
            .mapEvents(
                eventMapper = { Internal.SuccessDeleteFromAdapter(parkingSpots = it) },
                errorMapper = { Internal.ErrorDeleteParkingSpot}
            )
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor()
)