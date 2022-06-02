package com.example.parking.data.network.api

import com.example.parking.data.network.modelJSON.ReservationJson
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import kotlin.collections.HashMap


const val RESERVATIONS_URL: String = "/reservations"
const val RESERVATIONS_DETAIL_URL: String = "/reservations/{id}"

interface ReservationAPI {
    @GET(RESERVATIONS_URL)
    fun getReservations(): Single<Response<List<ReservationJson>>>

    @POST(RESERVATIONS_URL)
    fun createReservation(@Body reservation: HashMap<String, Any>): Single<Response<ReservationJson>>

    @DELETE(RESERVATIONS_URL)
    fun deleteReservations(): Single<Response<ResponseBody>>

    @GET(RESERVATIONS_DETAIL_URL)
    fun getReservationDetail(@Path("id") id: String): Single<Response<ReservationJson>>

    @PUT(RESERVATIONS_DETAIL_URL)
    fun updateReservationDetail(@Path("id") id: String, @Body reservation: HashMap<String, Any>): Single<Response<ReservationJson>>

    @DELETE(RESERVATIONS_DETAIL_URL)
    fun deleteReservationDetail(@Path("id") id: String): Single<Response<ResponseBody>>
}