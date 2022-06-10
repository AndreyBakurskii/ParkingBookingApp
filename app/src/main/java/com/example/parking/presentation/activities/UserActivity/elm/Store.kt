package com.example.parking.presentation.activities.UserActivity.elm

import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.mapper.ParkingSpotMapper
import com.example.parking.data.models.Reservation
import com.example.parking.data.network.NetworkService
import com.example.parking.data.repository.CarRepository
import com.example.parking.data.repository.ParkingSpotRepository
import com.example.parking.data.repository.ReservationRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

import com.example.parking.presentation.activities.UserActivity.elm.Event.Ui
import com.example.parking.presentation.activities.UserActivity.elm.Event.Internal
import com.example.parking.presentation.utils.statusCodeHandler
import com.example.parking.utils.toDate
import io.reactivex.Observable
import retrofit2.Retrofit
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import java.util.*
import kotlin.collections.ArrayList

class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {
    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessLoadReservationsJson -> {
            state { copy(loading = true, doUpdate = false, reservationsJson = event.reservationsJson) }
            commands { +Command.LoadAllCars }
        }
        is Internal.SuccessLoadCars -> {
            state { copy(loading = true, doUpdate = false, cars = event.cars) }
            commands { +Command.LoadAllParkingSpots }
        }
        is Internal.SuccessLoadParkingSpots -> {
            state { copy(loading = true, doUpdate = false, parkingSpots = event.parkingSpots) }
            commands { +Command.InitReservations(
                reservationsJson = state.reservationsJson,
                cars = state.cars,
                employee = state.employee!!,
                parkingSpots = state.parkingSpots
            ) }
        }
        is Internal.SuccessInitReservations -> {
            state { copy(loading = false, doUpdate = true, reservations = event.reservations) }
        }
        is Internal.ErrorLoadReservations -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorLoadReservations }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false, doUpdate = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }

    override fun Result.ui(event: Ui) = when (event){
        is Ui.Init -> { }
        is Ui.LoadReservations -> {
            state { copy(loading = true, doUpdate = false, employee = event.employee)}
            commands { +Command.LoadAllReservations }
        }
        is Ui.ClickCreateReservation -> {
            state {copy(loading = false, doUpdate = false)}
            effects { +Effect.ToCreateReservationFragment }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val retrofit: Retrofit = NetworkService("user", "password").retrofit

    private val reservationRepository: ReservationRepository = ReservationRepository(retrofit)
    private val carRepository: CarRepository = CarRepository(retrofit)
    private val parkingSpotRepository: ParkingSpotRepository = ParkingSpotRepository(retrofit)

    private val carMapper: CarMapper = CarMapper()
    private val parkingSpotMapper: ParkingSpotMapper = ParkingSpotMapper()

    private val dateTimePattern: String = "yyyy-MM-dd'T'HH:mm:ss"

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadAllReservations -> reservationRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadReservationsJson(
                        reservationsJson = ArrayList(it)
                    ) },
                    errorHandler = { Internal.ErrorLoadReservations }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.LoadAllCars -> carRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadCars(
                        cars = ArrayList(it
                            .map { carJson -> carMapper.fromJsonToModel(carJson) })
                    ) },
                    errorHandler = { Internal.ErrorLoadReservations }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.LoadAllParkingSpots -> parkingSpotRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadParkingSpots(
                        parkingSpots = ArrayList(it
                            .map { parkingSpotJson -> parkingSpotMapper.fromJsonToModel(parkingSpotJson) })
                    ) },
                    errorHandler = { Internal.ErrorLoadReservations }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.InitReservations -> {
            Observable.fromArray(
                ArrayList(
                    command.reservationsJson
                        .filter { reservationJson -> reservationJson.employeeId == command.employee.id.toString() }
                        .map { reservationJson -> Reservation(
                            id = UUID.fromString(reservationJson.id),
                            car = command.cars
                                .filter { car -> car.id.toString() == reservationJson.carId }[0],
                            employee = command.employee,
                            parkingSpot = command.parkingSpots
                                .filter { parkingSpot -> parkingSpot.id.toString() == reservationJson.parkingSpotId }[0],
                            startTime = reservationJson.startTime.toDate(dateTimePattern),
                            endTime = reservationJson.endTime.toDate(dateTimePattern)
                        ) }
                        .sortedBy { reservation -> reservation.startTime }
                )
            )
                .mapEvents(
                    eventMapper = { Internal.SuccessInitReservations(reservations = it) },
                    errorMapper = { Internal.ErrorLoadReservations }
                )
        }
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor()
)