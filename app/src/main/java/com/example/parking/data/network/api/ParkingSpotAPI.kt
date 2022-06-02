package com.example.parking.data.network.api

import com.example.parking.data.network.modelJSON.ParkingSpotJson
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


const val PARKING_SPOT_URL: String = "/parkingSpots"
const val PARKING_SPOT_DETAIL_URL: String = "/parkingSpots/{id}"

interface ParkingSpotAPI {
    @GET(PARKING_SPOT_URL)
    fun getParkingSpots(): Single<Response<List<ParkingSpotJson>>>

    @POST(PARKING_SPOT_URL)
    fun createParkingSpot(@Body parkingSpot: HashMap<String, Any>): Single<Response<ParkingSpotJson>>

    @DELETE(PARKING_SPOT_URL)
    fun deleteParkingSpots(): Single<Response<ResponseBody>>
    
    @GET(PARKING_SPOT_DETAIL_URL)
    fun getParkingSpotDetail(@Path("id") id: String): Single<Response<ParkingSpotJson>>

    @PUT(PARKING_SPOT_DETAIL_URL)
    fun updateParkingSpotDetail(@Path("id") id: String, @Body parkingSpot: HashMap<String, Any>): Single<Response<ParkingSpotJson>>

    @DELETE(PARKING_SPOT_DETAIL_URL)
    fun deleteParkingSpotDetail(@Path("id") id: String): Single<Response<ResponseBody>>
}