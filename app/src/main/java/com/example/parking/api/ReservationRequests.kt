package com.example.parking.api

import retrofit2.Call
import retrofit2.http.*


const val RESERVATIONS_URL: String = "/reservations"
const val RESERVATIONS_DETAIL_URL: String = "/reservations/{id}"

interface ReservationRequests {
    @GET(RESERVATIONS_URL)
    fun getReservations(): Call<ReservationsJson>

    @POST(RESERVATIONS_URL)
    fun createReservation(@Body car: ReservationsJson): Call<ReservationsJson>

    // todo: возращается просто статус
    @DELETE(RESERVATIONS_URL)
    fun deleteReservations(): Call<ReservationsJson>

    @GET(RESERVATIONS_DETAIL_URL)
    fun getReservationDetail(@Path("id") id: String): Call<ReservationsJson>

    @PUT(RESERVATIONS_DETAIL_URL)
    fun updateReservationDetail(@Path("id") id: String, @Body car: ReservationsJson): Call<ReservationsJson>

    // todo: возращается просто статус
    @DELETE(RESERVATIONS_DETAIL_URL)
    fun deleteReservationDetail(@Path("id") id: String): Call<ReservationsJson>
}