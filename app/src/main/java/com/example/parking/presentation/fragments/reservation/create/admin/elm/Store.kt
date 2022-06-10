package com.example.parking.presentation.fragments.reservation.create.admin.elm

import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.mapper.EmployeeMapper
import com.example.parking.data.mapper.ParkingSpotMapper
import com.example.parking.data.models.Car
import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.models.Reservation
import com.example.parking.data.network.NetworkService
import com.example.parking.data.network.modelJSON.CarJson
import com.example.parking.data.network.modelJSON.ReservationJson
import com.example.parking.data.repository.CarRepository
import com.example.parking.data.repository.EmployeeRepository
import com.example.parking.data.repository.ParkingSpotRepository
import com.example.parking.data.repository.ReservationRepository
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import com.example.parking.presentation.fragments.reservation.create.admin.elm.Event.Ui
import com.example.parking.presentation.fragments.reservation.create.admin.elm.Event.Internal
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
        is Ui.CreateClick -> {
            state { copy(loading = false) }
            effects { +Effect.ShowConfirmDialog(
                event.carModel, event.carRegistryNumber,
                event.employee,
                event.startTime, event.endTime
            ) }
        }
        is Ui.OkClickConfirmDialog -> {
            state { copy(
                loading = true,
                employee = event.employee,
                carModel = event.carModel, carRegistryNumber = event.carRegistryNumber,
                startTime = event.startTime, endTime = event.endTime
                ) }
            commands { +Command.CheckExistEmployee(event.employee) }
        }
    }

    override fun Result.internal(event: Internal) = when (event) {
        is Internal.SuccessCreateReservation -> {
            state { copy(loading = false) }
            effects { +Effect.ToReservationsFragment }
        }
        is Internal.ErrorCreateReservation -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorCreateReservation }
        }
        is Internal.SuccessCheckEmployee -> {
            state { copy(loading = true) }
            commands { +Command.CheckExistCar(state.carModel, state.carRegistryNumber) }
        }
        is Internal.ErrorNotFoundEmployee -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNotFoundEmployee }
        }
        is Internal.SuccessCheckCar -> {
            state { copy(loading = true, reservedCar = event.car) }
            commands { +Command.LoadAllReservations }
        }
        is Internal.ErrorNotFoundCar -> {
            state { copy(loading = false) }
            effects { +Effect.ShowErrorNotFoundCar }
        }
        is Internal.SuccessLoadAllReservations -> {
            state { copy(loading = true, reservations = event.reservations) }
            commands { +Command.LoadAllParkingSpots }
        }
        is Internal.SuccessLoadAllParkingSpots -> {
            state { copy(loading = true, parkingSpots = event.parkingSpots) }
            commands {
                +Command.GetFreeParkingSpots(
                    reservations = state.reservations,
                    parkingSpots = event.parkingSpots,
                    startTime = state.startTime!!, endTime = state.endTime!!
                )
            }
        }
        is Internal.SuccessGetFreeParkingSpots -> {
            state { copy(loading = true, reservedParkingSpot = event.parkingSpot) }
            commands {
                +Command.CreateReservation(
                    employee = state.employee!!,
                    car = state.reservedCar!!,
                    parkingSpot = event.parkingSpot,
                    startTime = state.startTime!!, endTime = state.endTime!!
                )
            }
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
        is Command.CheckExistEmployee -> employeeRepository
            .getEmployee(command.employee.id.toString())
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessCheckEmployee },
                    errorHandler = { Internal.ErrorNotFoundEmployee }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.CheckExistCar -> carRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = {
                        return@statusCodeHandler getCarByModelAndRegistryNumber(
                            it,
                            command.carModel,
                            command.carRegistryNumber
                        )?.let { car -> Internal.SuccessCheckCar(car) } ?: Internal.ErrorNotFoundCar
                                     },
                    errorHandler = { Internal.ErrorCreateReservation }
                )},
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.LoadAllReservations -> reservationRepository
            .getAll()
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessLoadAllReservations(
                        reservations = ArrayList(it)
                    ) },
                    errorHandler = { Internal.ErrorCreateReservation }
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
                    errorHandler = { Internal.ErrorCreateReservation }
                ) },
                errorMapper = { Internal.ErrorNetwork }
            )
        is Command.GetFreeParkingSpots -> {
            Observable.fromArray(
                getFreeParkingSpots(
                    reservations = command.reservations,
                    parkingSpots = command.parkingSpots,
                    startTime = command.startTime, endTime = command.endTime
                )
            ).mapEvents(
                eventMapper = {
                        freeParkingSpots ->
                    if (freeParkingSpots.isNotEmpty()) Internal.SuccessGetFreeParkingSpots(freeParkingSpots[0])
                    else  Internal.ErrorNotFreeParkingSpots
                },
                errorMapper = { Internal.ErrorCreateReservation }
            )
        }
        is Command.CreateReservation -> reservationRepository
            .createReservation(
                carId = command.car.id.toString(),
                employeeId = command.employee.id.toString(),
                parkingSpotId = command.parkingSpot.id.toString(),
                startTime = command.startTime.toStr(dateTimePattern, TimeZone.getTimeZone("GMT")),
                endTime = command.endTime.toStr(dateTimePattern, TimeZone.getTimeZone("GMT"))
            )
            .mapEvents(
                eventMapper = { response -> response.statusCodeHandler(
                    successHandler = { Internal.SuccessCreateReservation },
                    errorHandler = { Internal.ErrorCreateReservation }
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

        var reservedParkingSpotIds: List<String> = reservations.filter {
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

