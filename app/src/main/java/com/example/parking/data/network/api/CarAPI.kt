package com.example.parking.data.network.api

import com.example.parking.data.network.modelJSON.CarJson
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


const val CARS_URL: String = "/cars"
const val CARS_DETAIL_URL: String = "/cars/{id}"

interface CarAPI {
    @GET(CARS_URL)
    fun getCars(): Single<Response<List<CarJson>>>

    @POST(CARS_URL)
    fun createCar(@Body car: HashMap<String, Any>): Single<Response<CarJson>>

    @DELETE(CARS_URL)
    fun deleteCars(): Single<Response<ResponseBody>>

    @GET(CARS_DETAIL_URL)
    fun getCarDetail(@Path("id") id: String): Single<Response<CarJson>>

    @PUT(CARS_DETAIL_URL)
    fun updateCarDetail(@Path("id") id: String, @Body car: HashMap<String, Any>): Single<Response<CarJson>>

    @DELETE(CARS_DETAIL_URL)
    fun deleteCarDetail(@Path("id") id: String): Single<Response<ResponseBody>>
}