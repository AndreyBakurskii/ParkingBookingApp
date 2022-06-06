package com.example.parking.data.network.api

import com.example.parking.data.network.modelJSON.EmployeeJson
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


const val EMPLOYEES_URL: String = "/employees"
const val EMPLOYEES_DETAIL_URL: String = "/employees/{id}"

interface EmployeeAPI {
    @GET(EMPLOYEES_URL)
    fun getEmployees(): Single<Response<List<EmployeeJson>>>

    @POST(EMPLOYEES_URL)
    fun createEmployee(@Body employee: HashMap<String, String>): Single<Response<EmployeeJson>>

    // todo: возращается просто статус
    @DELETE(EMPLOYEES_URL)
    fun deleteEmployees(): Single<Response<ResponseBody>>

    @GET(EMPLOYEES_DETAIL_URL)
    fun getEmployeeDetail(@Path("id") id: String): Single<Response<EmployeeJson>>

    @PUT(EMPLOYEES_DETAIL_URL)
    fun updateEmployeeDetail(@Path("id") id: String, @Body employee: HashMap<String, String>): Single<Response<EmployeeJson>>

    // todo: возращается просто статус
    @DELETE(EMPLOYEES_DETAIL_URL)
    fun deleteEmployeeDetail(@Path("id") id: String): Single<Response<ResponseBody>>
}