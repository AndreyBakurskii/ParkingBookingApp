package com.example.parking.data.repository

import android.util.Log
import com.example.parking.data.network.modelJSON.EmployeeJson
import com.example.parking.data.network.api.EmployeeAPI
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.*

class EmployeeRepository(private val retrofit: Retrofit) {

    private val employeeAPI: EmployeeAPI = retrofit.create(EmployeeAPI::class.java)
    
    fun getEmployee(id: String): Observable<Response<EmployeeJson>> {
        return employeeAPI.getEmployeeDetail(id)
            .toObservable()
    }

    fun getAll(): Observable<Response<List<EmployeeJson>>>{
        return employeeAPI.getEmployees()
            .toObservable()
    }

    fun createEmployee(employee: HashMap<String, String>): Observable<Response<EmployeeJson>> {
        return employeeAPI.createEmployee(employee)
            .toObservable()
    }

    fun updateEmployee(id: String, employee: HashMap<String, String>): Observable<Response<EmployeeJson>> {
        return employeeAPI.updateEmployeeDetail(
            id,
            employee
        )
            .toObservable()
    }

    fun deleteAll(): Observable<Response<ResponseBody>> {
        return employeeAPI.deleteEmployees()
            .toObservable()
    }

    fun deleteEmployee(id: String): Observable<Response<ResponseBody>> {
        return employeeAPI.deleteEmployeeDetail(id)
            .toObservable()
    }
}