package com.example.parking.data.network.api

import com.example.parking.data.network.modelJSON.ParkingSpotJson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


const val PARKING_SPOT_URL: String = "/parkingSpots"
const val PARKING_SPOT_DETAIL_URL: String = "/parkingSpots/{id}"

interface ParkingSpotAPI {
    @GET(PARKING_SPOT_URL)
    fun getParkingSpots(): Call<MutableList<ParkingSpotJson>>

    @POST(PARKING_SPOT_URL)
    fun createParkingSpot(@Body parkingSpot: HashMap<String, Any>): Call<ParkingSpotJson>

    // todo: возращается просто статус
    @DELETE(PARKING_SPOT_URL)
    fun deleteParkingSpots(): Call<ResponseBody>
    
    @GET(PARKING_SPOT_DETAIL_URL)
    fun getParkingSpotDetail(@Path("id") id: String): Call<ParkingSpotJson>

    @PUT(PARKING_SPOT_DETAIL_URL)
    fun updateParkingSpotDetail(@Path("id") id: String, @Body parkingSpot: HashMap<String, Any>): Call<ParkingSpotJson>

    // todo: возращается просто статус
    @DELETE(PARKING_SPOT_DETAIL_URL)
    fun deleteParkingSpotDetail(@Path("id") id: String): Call<ResponseBody>
}