package com.example.parking.presentation.fragments.employee.list.elm

import com.example.parking.data.models.Employee

data class State(
    val loading: Boolean = false,
    val employees: ArrayList<Employee> = arrayListOf(),
    val doUpdate: Boolean = false
)

sealed class Effect {
    object ShowErrorLoadEmployees : Effect()
    object ShowErrorDeleteEmployee : Effect()
    object ShowErrorNetwork : Effect()
    object ToCreateEmployeeFragment : Effect()
    data class ShowDeleteDialog(var employee: Employee, var positionInAdapter: Int) : Effect()
}

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        object LoadEmployees : Ui()
        data class ClickDeleteEmployee(var employee: Employee, var positionInAdapter: Int) : Ui()
        data class OkClickDeleteDialog(var employee: Employee, var positionInAdapter: Int) : Ui()
        object ClickCreateEmployee : Ui()
    }

    sealed class Internal : Event() {
        data class SuccessLoadEmployees(var employees: ArrayList<Employee>) : Internal()
        object ErrorLoadEmployees : Internal()

        data class SuccessDeleteFromServer(val employee: Employee, val positionInAdapter: Int) : Internal()
        data class SuccessDeleteFromAdapter(val employees: ArrayList<Employee>) : Internal()
        object ErrorDeleteEmployee : Internal()

        object ErrorNetwork : Internal()
    }
}

sealed class Command {
    object LoadAllEmployee : Command()
    data class DeleteFromServer(var employee: Employee, var positionInAdapter: Int) : Command()
    data class DeleteFromAdapter(var employees: ArrayList<Employee>, var positionInAdapter: Int) : Command()
}