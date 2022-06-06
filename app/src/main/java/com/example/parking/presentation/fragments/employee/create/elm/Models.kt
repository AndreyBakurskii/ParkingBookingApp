package com.example.parking.presentation.fragments.employee.create.elm

import com.example.parking.data.models.Employee

data class State(
    val loading: Boolean = false
)

sealed class Effect {
    data class ShowConfirmDialog(var employee: Employee) : Effect()
    object ShowErrorCreateEmployee : Effect()
    object ShowErrorEmployeeExist : Effect()
    object ShowErrorNetwork : Effect()
    object ToEmployeesFragment : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        data class CreateClick(var employee: Employee) : Ui()
        data class OkClickConfirmDialog(var employee: Employee) : Ui()
    }

    sealed class Internal : Event() {
        object SuccessCreateEmployee : Internal()
        object ErrorCreateEmployee : Internal()
        data class SuccessCheckExistEmployee(val employee: Employee) : Internal()
        object ErrorCheckExistEmployee : Internal()
        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    data class CheckExistEmployee(var employee: Employee) : Command()
    data class CreateEmployee(var employee: Employee) : Command()
}