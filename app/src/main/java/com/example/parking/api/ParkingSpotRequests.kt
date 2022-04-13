package com.example.parking.api

import retrofit2.Call
import retrofit2.http.*


const val PARKING_SPOT_URL: String = "/parkingSpots"
const val PARKING_SPOT_DETAIL_URL: String = "/parkingSpots/{id}"

interface ParkingSpotRequests {
    @GET(PARKING_SPOT_URL)
    fun getParkingSpots(): Call<ParkingSpotJson>

    @POST(PARKING_SPOT_URL)
    fun createParkingSpot(@Body car: ParkingSpotJson): Call<ParkingSpotJson>

    // todo: возращается просто статус
    @DELETE(PARKING_SPOT_URL)
    fun deleteParkingSpots(): Call<ParkingSpotJson>
    
    @GET(PARKING_SPOT_DETAIL_URL)
    fun getParkingSpotDetail(@Path("id") id: String): Call<ParkingSpotJson>

    @PUT(PARKING_SPOT_DETAIL_URL)
    fun updateParkingSpotDetail(@Path("id") id: String, @Body car: ParkingSpotJson): Call<ParkingSpotJson>

    // todo: возращается просто статус
    @DELETE(PARKING_SPOT_DETAIL_URL)
    fun deleteParkingSpotDetail(@Path("id") id: String): Call<ParkingSpotJson>
}