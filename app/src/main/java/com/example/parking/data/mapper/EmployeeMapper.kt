package com.example.parking.data.mapper

import com.example.parking.data.models.Employee
import com.example.parking.data.network.modelJSON.EmployeeJson
import java.util.*

class EmployeeMapper {
    fun fromJsonToModel(jsonModel: EmployeeJson): Employee {
        return Employee(
            id = UUID.fromString(jsonModel.id),
            name = jsonModel.name
        )
    }
    fun fromHashMapToModel(hashMapModel: HashMap<String, String>): Employee {
        return Employee(
            id = UUID.fromString(hashMapModel["id"].toString()),
            name = hashMapModel["name"].toString()
        )
    }
}