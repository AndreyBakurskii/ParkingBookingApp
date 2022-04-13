package com.example.parking.api

import retrofit2.Call
import retrofit2.http.*


const val CARS_URL: String = "/cars"
const val CARS_DETAIL_URL: String = "/cars/{id}"

interface CarsRequests {
    @GET(CARS_URL)
    fun getCars(): Call<CarsJson>

    @POST(CARS_URL)
    fun createCar(@Body car: CarsJson): Call<CarsJson>

    // todo: возращается просто статус
    @DELETE(CARS_URL)
    fun deleteCars(): Call<CarsJson>

    @GET(CARS_DETAIL_URL)
    fun getCarDetail(@Path("id") id: String): Call<CarsJson>

    @PUT(CARS_DETAIL_URL)
    fun updateCarDetail(@Path("id") id: String, @Body car: CarsJson): Call<CarsJson>

    // todo: возращается просто статус
    @DELETE(CARS_DETAIL_URL)
    fun deleteCarDetail(@Path("id") id: String): Call<CarsJson>
}