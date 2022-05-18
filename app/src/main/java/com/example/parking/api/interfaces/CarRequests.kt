package com.example.parking.api.interfaces

import com.example.parking.api.dataclasses.CarJson
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


const val CARS_URL: String = "/cars"
const val CARS_DETAIL_URL: String = "/cars/{id}"

interface CarRequests {
    @GET(CARS_URL)
    fun getCars(): Single<List<CarJson>>

    @POST(CARS_URL)
    fun createCar(@Body car: HashMap<String, Any>): Call<CarJson>

    // todo: возращается просто статус
    @DELETE(CARS_URL)
    fun deleteCars(): Call<ResponseBody>

    @GET(CARS_DETAIL_URL)
    fun getCarDetail(@Path("id") id: String): Call<CarJson>

    @PUT(CARS_DETAIL_URL)
    fun updateCarDetail(@Path("id") id: String, @Body car: HashMap<String, Any>): Call<CarJson>

    // todo: возращается просто статус
    @DELETE(CARS_DETAIL_URL)
    fun deleteCarDetail(@Path("id") id: String): Call<ResponseBody>
}