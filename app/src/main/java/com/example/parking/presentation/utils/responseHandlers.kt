package com.example.parking.presentation.utils

import android.database.DatabaseErrorHandler
import retrofit2.Response


fun <T, E> Response<T>.statusCodeHandler(successHandler: (T) -> E, errorHandler: () -> E) : E {
    return when {
        this.isSuccessful -> successHandler(this.body()!!)
        else -> errorHandler()
    }
}

// пример обработки команды, комментарий будет удален по окончанию работы
//override fun execute(command: Command): Observable<Internal> = when (command) {
//        is Command.getAllCars -> CarRepository(
//            NetworkService("admin", "password").retrofit
//        )
//            .getCar("24319def-1441-4843-883a-fcc33e054233")
//            .mapEvents(
//                eventMapper = { response -> response.statusCodeHandler(
//                    successHandler = { Internal.SuccessLoadCar(car = it) },
//                    errorHandler = { Internal.ErrorInvalidPassword }
//                )},
//                errorMapper = { Internal.ErrorInvalidUserEmail }
//            )
//    }