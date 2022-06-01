package com.example.parking.data.mapper

import com.example.parking.data.models.Car
import com.example.parking.data.network.modelJSON.CarJson
import java.util.*

class CarMapper {
    fun fromJsonToModel(jsonModel: CarJson): Car {
        return Car(
            id = UUID.fromString(jsonModel.id),
            model = jsonModel.model,
            length = jsonModel.length,
            wight = jsonModel.wight,
            registryNumber = jsonModel.registryNumber
        )
    }

    fun fromModelToJson(car: Car): CarJson {
        return CarJson(
            id = car.id.toString(),
            model = car.model,
            registryNumber = car.registryNumber,
            wight = car.wight,
            length = car.length
        )
    }
}