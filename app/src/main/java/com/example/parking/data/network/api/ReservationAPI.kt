package com.example.parking.data.network.api

import com.example.parking.data.network.modelJSON.ReservationJson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


const val RESERVATIONS_URL: String = "/reservations"
const val RESERVATIONS_DETAIL_URL: String = "/reservations/{id}"

interface ReservationAPI {
    @GET(RESERVATIONS_URL)
    fun getReservations(): Call<MutableList<ReservationJson>>

    @POST(RESERVATIONS_URL)
    fun createReservation(@Body reservation: HashMap<String, Any>): Call<ReservationJson>

    // todo: возращается просто статус
    @DELETE(RESERVATIONS_URL)
    fun deleteReservations(): Call<ResponseBody>

    @GET(RESERVATIONS_DETAIL_URL)
    fun getReservationDetail(@Path("id") id: String): Call<ReservationJson>

    @PUT(RESERVATIONS_DETAIL_URL)
    fun updateReservationDetail(@Path("id") id: String, @Body reservation: HashMap<String, Any>): Call<ReservationJson>

    // todo: возращается просто статус
    @DELETE(RESERVATIONS_DETAIL_URL)
    fun deleteReservationDetail(@Path("id") id: String): Call<ResponseBody>
}