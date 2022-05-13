package com.example.parking.api.interfaces

import com.example.parking.api.dataclasses.EmployeeJson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


const val EMPLOYEES_URL: String = "/employees"
const val EMPLOYEES_DETAIL_URL: String = "/employees/{id}"

interface EmployeeRequests {
    @GET(EMPLOYEES_URL)
    fun getEmployees(): Call<MutableList<EmployeeJson>>

    @POST(EMPLOYEES_URL)
    fun createEmployee(@Body employee: HashMap<String, Any>): Call<EmployeeJson>

    // todo: возращается просто статус
    @DELETE(EMPLOYEES_URL)
    fun deleteEmployees(): Call<ResponseBody>

    @GET(EMPLOYEES_DETAIL_URL)
    fun getEmployeeDetail(@Path("id") id: String): Call<EmployeeJson>

    @PUT(EMPLOYEES_DETAIL_URL)
    fun updateEmployeeDetail(@Path("id") id: String, @Body employee: HashMap<String, Any>): Call<EmployeeJson>

    // todo: возращается просто статус
    @DELETE(EMPLOYEES_DETAIL_URL)
    fun deleteEmployeeDetail(@Path("id") id: String): Call<ResponseBody>
}