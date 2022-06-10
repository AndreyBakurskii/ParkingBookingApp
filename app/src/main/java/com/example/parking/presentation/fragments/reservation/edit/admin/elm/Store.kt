package com.example.parking.presentation.fragments.reservation.edit.admin.elm

import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.mapper.ParkingSpotMapper
import com.example.parking.data.models.Car
import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.network.NetworkService
import com.example.parking.data.network.modelJSON.CarJson
import com.example.parking.data.network.modelJSON.ReservationJson
import com.example.parking.data.repository.CarRepository
import com.example.parking.data.repository.EmployeeRepository
import com.example.parking.data.repository.ParkingSpotRepository
import com.example.parking.data.repository.ReservationRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.presentation.fragments.reservation.edit.admin.elm.Event.Internal
import com.example.parking.presentation.fragments.reservation.edit.admin.elm.Event.Ui
import com.example.parking.presentation.utils.statusCodeHandler
import com.example.parking.utils.toDate
import com.example.parking.utils.toStr
import io.reactivex.Observable
import retrofit2.Retrofit
import vivid.money.elmslie.core.Actor
import vivid.money.elmslie.core.ElmStoreCompat
import java.util.*
import kotlin.collections.ArrayList

class Reducer : ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(Ui::class, Internal::class) {
    override fun Result.ui(event: Ui) = when (event) {
        is Ui.Init -> {}
        is Ui.UpdateClick -> {
            state { copy(loading = false) }
            effects { +Effect.ShowConfirmDialog(
                event.reservation,
                event.updatedCarModel,
                event.updatedCarRegistryNumber,
                event.updatedEmployee,
                event.updatedStartTime,
                event.updatedEndTime
            ) }
        }
        is Ui.OkClickConfirmDialog -> {
            state { copy(
                loading = true,
                reservation = event.reservation,
                updatedCarModel = event.updatedCarModel,
                updatedCarRegistryNumber = event.updatedCarRegistryNumber,
                updatedEmployee = event.updatedEmployee,
                updatedStartTime = event.updatedStartTime,
                updatedEndTime = event.updatedEndTime
                ) }
            commands { +Command.CheckExistEmployee(
                state.reservation!!.employee,
                event.updatedEmployee
            ) }
        }
    }

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessUpdateReservation -> {
            state { copy(loading = false) }
            effects { +Effect.ToReservationsFragment }
        }
        is Internal.ErrorUpdateReservation -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorUpdateReservation }
        }
        is Internal.SuccessCheckEmployee -> {
            state { copy(loading = true) }
            commands { +Command.CheckExistCar(
                state.reservation!!.car,
                state.updatedCarModel,
                state.updatedCarRegistryNumber)
            }
        }
        is Internal.ErrorNotFoundEmployee -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNotFoundEmployee }
        }
        is Internal.SuccessCheckCar -> {
            state { copy(loading = true, reservedCar = event.car) }
            commands { +Command.CheckNeedNewParkingSpot(
                currentParkingSpot = state.reservation!!.parkingSpot,
                currentStartTime = state.reservation!!.startTime,
                currentEndTime = state.reservation!!.endTime,
                updatedStartTime = state.updatedStartTime!!,
                updatedEndTime = state.updatedEndTime!!
            ) }
        }
        is Internal.ErrorNotFoundCar -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNotFoundCar }
        }
        is Internal.NeedNewParkingSpot -> {
            state { copy(loading = true) }
            commands { +Command.LoadAllReservations }
        }
        is Internal.NoNeedNewParkingSpot -> {
            state { copy(loading = true, reservedParkingSpot = state.reservation!!.parkingSpot) }
            commands { Command.UpdateReservation(
                reservationId = state.reservation!!.id.toString(),
                updatedEmployee = state.updatedEmployee!!,
                reservedCar = state.reservedCar!!,
                reservedParkingSpot = state.reservedParkingSpot!!,
                updatedStartTime = state.updatedStartTime!!,
                updatedEndTime = state.updatedEndTime!!
            ) }
        }
        is Internal.SuccessLoadAllReservations -> {
            state { copy(loading = true, reservations = event.reservations) }
            commands { +Command.LoadAllParkingSpots }
        }
        is Internal.SuccessLoadAllParkingSpots -> {
            state { copy(loading = true, parkingSpots = event.parkingSpots) }
            commands { +Command.GetFreeParkingSpots(
                reservation = state.reservation!!,
                reservations = state.reservations,
                parkingSpots = event.parkingSpots,
                updatedStartTime = state.updatedStartTime!!,
                updatedEndTime = state.updatedEndTime!!
            ) }
        }
        is Internal.SuccessGetFreeParkingSpots -> {
            state { copy(loading = true, reservedParkingSpot = event.parkingSpot) }
            commands { +Command.UpdateReservation(
                reservationId = state.reservation!!.id.toString(),
                updatedEmployee = state.updatedEmployee!!,
                reservedCar = state.reservedCar!!,
                reservedParkingSpot = state.reservedParkingSpot!!,
                updatedStartTime = state.updatedStartTime!!,
                updatedEndTime = state.updatedEndTime!!,
            ) }
        }
        is Internal.ErrorNotFreeParkingSpots -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNotFreeParkingSpots }
        }
        is Internal.ErrorNetwork -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNetwork }
        }
    }
}

class MyActor : Actor<Command, Event> {
    private val retrofit: Retrofit = NetworkService("admin", "password").retrofit

    private val reservationRepository: ReservationRepository = ReservationRepository(retrofit)
    private val carRepository: CarRepository = CarRepository(retrofit)
    private val employeeRepository: EmployeeRepository = EmployeeRepository(retrofit)
    private val parkingSpotRepository: ParkingSpotRepository = ParkingSpotRepository(retrofit)

    private val carMapper: CarMapper = CarMapper()
    private val parkingSpotMapper: ParkingSpotMapper = ParkingSpotMapper()

    private val dateTimePattern: String = "yyyy-MM-dd'T'HH:mm:ss"

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.CheckExistEmployee ->
            if (command.currentEmployee.name == command.updatedEmployee.name) {
                Observable.just(Internal.SuccessCheckEmployee)
            } else {
                employeeRepository
                    .getEmployee(command.updatedEmployee.id.toString())
                    .mapEvents(
                        eventMapper = { response -> response.statusCodeHandler(
                            successHandler = { Internal.SuccessCheckEmployee },
                            errorHandler = { Internal.ErrorNotFoundEmployee }
                        ) },
                        errorMapper = { Internal.ErrorNetwork }
                    )
                }
        is Command.CheckExistCar ->
            if (command.currentCar.model == command.updatedCarModel
                && command.currentCar.registryNumber == command.updatedCarRegistryNumber) {
                    Observable.just(Internal.SuccessCheckCar(command.currentCar))
            } else {
                carRepository
                    .getAll()
                    .mapEvents(
                        eventMapper = { response -> response.statusCodeHandler(
                            successHandler = {
                                return@statusCodeHandler getCarByModelAndRegistryNumber(
                                    it,
                                    command.updatedCarModel,
                                    command.updatedCarRegistryNumber
                                )?.let { car -> Internal.SuccessCheckCar(car) } ?: Internal.ErrorNotFoundCar
                            },
                            errorHandler = { Internal.ErrorUpdateReservation }
                        ) },
                        errorMapper = { Internal.ErrorNetwork }
                    )
                }
        is Command.CheckNeedNewParkingSpot ->
            if ((command.currentStartTime <= command.updatedStartTime && command.updatedStartTime <= command.currentEndTime)
                && (command.currentStartTime <= command.updatedEndTime && command.updatedEndTime <= command.currentEndTime)
            ) {
                Observable.just(Internal.NoNeedNewParkingSpot(command.currentParkingSpot))
            } else {
                Observable.just(Internal.NeedNewParkingSpot)
            }
        is Command.LoadAllReservations -> reservationRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadAllReservations(
                        reservations = ArrayList(it)
                    ) },
                    errorHandler = { Internal.ErrorUpdateReservation }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.LoadAllParkingSpots -> parkingSpotRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadAllParkingSpots(
                        parkingSpots = ArrayList(
                            it.map { parkingSpotJson ->  parkingSpotMapper.fromJsonToModel(parkingSpotJson) })
                    ) },
                    errorHandler = { Internal.ErrorUpdateReservation }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.GetFreeParkingSpots -> {
            Observable.fromArray(
                getFreeParkingSpots(
                    reservations = ArrayList(command.reservations.filter { it.id == command.reservation.id.toString() }),
                    parkingSpots = command.parkingSpots,
                    startTime = command.updatedStartTime,
                    endTime = command.updatedEndTime
                )
            ).mapEvents(
                eventMapper = {
                        freeParkingSpots ->
                    if (freeParkingSpots.isNotEmpty()) Internal.SuccessGetFreeParkingSpots(freeParkingSpots[0])
                    else  Internal.ErrorNotFreeParkingSpots
                },
                errorMapper = { Internal.ErrorUpdateReservation }
            )
        }
        is Command.UpdateReservation -> reservationRepository
            .updateReservation(
                id = command.reservationId,
                carId = command.reservedCar.id.toString(),
                employeeId = command.updatedEmployee.id.toString(),
                parkingSpotId = command.reservedParkingSpot.id.toString(),
                startTime = command.updatedStartTime.toStr(dateTimePattern, TimeZone.getTimeZone("GMT")),
                endTime = command.updatedEndTime.toStr(dateTimePattern, TimeZone.getTimeZone("GMT"))
            )
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessUpdateReservation },
                    errorHandler = { Internal.ErrorUpdateReservation }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
    }

    private fun getCarByModelAndRegistryNumber(
        cars: List<CarJson>,
        carModel: String,
        carRegistryNumber: String) : Car?
    {
        val matchingCars: List<CarJson> = cars
            .filter { it.model == carModel && it.registryNumber == carRegistryNumber }

        return if (matchingCars.isNotEmpty()) {
            carMapper.fromJsonToModel(matchingCars[0])
        } else {
            null
        }
    }

    private fun getFreeParkingSpots(
        reservations: ArrayList<ReservationJson>,
        parkingSpots: ArrayList<ParkingSpot>,
        startTime: Date, endTime: Date
    ) : ArrayList<ParkingSpot> {

        val reservedParkingSpotIds: List<String> = reservations.filter {
            (it.startTime.toDate(dateTimePattern) < startTime && startTime < it.endTime.toDate(dateTimePattern)) ||
            (it.startTime.toDate(dateTimePattern) < endTime && endTime < it.endTime.toDate(dateTimePattern))
        }
            .map { reservationJson -> reservationJson.parkingSpotId }

        return ArrayList(parkingSpots
            .filter { parkingSpot -> !reservedParkingSpotIds.contains(parkingSpot.id.toString()) }
        )
    }
}

fun storeFactory() = ElmStoreCompat(
    initialState = State(),
    reducer = Reducer(),
    actor = MyActor()
)
