package com.example.parking.api

import retrofit2.Call
import retrofit2.http.*


const val EMPLOYEES_URL: String = "/employees"
const val EMPLOYEES_DETAIL_URL: String = "/employees/{id}"

interface EmployeesRequests {
    @GET(EMPLOYEES_URL)
    fun getEmployees(): Call<EmployeesJson>

    @POST(EMPLOYEES_URL)
    fun createEmployee(@Body Employee: EmployeesJson): Call<EmployeesJson>

    // todo: возращается просто статус
    @DELETE(EMPLOYEES_URL)
    fun deleteEmployees(): Call<EmployeesJson>

    @GET(EMPLOYEES_DETAIL_URL)
    fun getEmployeeDetail(@Path("id") id: String): Call<EmployeesJson>

    @PUT(EMPLOYEES_DETAIL_URL)
    fun updateEmployeeDetail(@Path("id") id: String, @Body Employee: EmployeesJson): Call<EmployeesJson>

    // todo: возращается просто статус
    @DELETE(EMPLOYEES_DETAIL_URL)
    fun deleteEmployeeDetail(@Path("id") id: String): Call<EmployeesJson>
}