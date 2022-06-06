package com.example.parking.data.models;

import java.util.*
import kotlin.collections.HashMap

public class Employee(
    val id: UUID = UUID.fromString("0f29717c-37e4-4a93-9165-6baacae64e98"),
    val name: String
) {
    fun toHashMap(withID: Boolean = false) : HashMap<String, String> {
        return if (withID) hashMapOf(
            "id" to id.toString(),
            "name" to name
        ) else hashMapOf(
            "name" to name
        )
    }
}

fun createEmployee(name: String): Employee {
    return Employee(
        id = UUID.nameUUIDFromBytes(name.toByteArray()),
        name = name
    )
}
