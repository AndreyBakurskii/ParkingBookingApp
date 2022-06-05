package com.example.parking.data.repository

import com.example.parking.data.models.ParkingSpot
import com.example.parking.data.network.modelJSON.ParkingSpotJson
import com.example.parking.data.network.api.ParkingSpotAPI
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit


class ParkingSpotRepository(private val retrofit: Retrofit) {

    private val parkingSpotAPI: ParkingSpotAPI = retrofit.create(ParkingSpotAPI::class.java)


    fun getParkingSpot(id: String): Observable<Response<ParkingSpotJson>> {
        return parkingSpotAPI.getParkingSpotDetail(id)
            .toObservable()
    }

    fun getAll(): Observable<Response<List<ParkingSpotJson>>>{
        return parkingSpotAPI.getParkingSpots()
            .toObservable()
    }

    fun createParkingSpot(parkingSpot: HashMap<String, Any>): Observable<Response<ParkingSpotJson>> {
        return parkingSpotAPI.createParkingSpot(
            parkingSpot
        )
            .toObservable()
    }

    fun updateParkingSpot(id: String, parkingNumber: Int, isFree: Boolean): Observable<Response<ParkingSpotJson>> {
        return parkingSpotAPI.updateParkingSpotDetail(
            id,
            hashMapOf(
                "parkingNumber" to parkingNumber,
                "isFree" to isFree
            )
        )
            .toObservable()
    }

    fun deleteAll(): Observable<Response<ResponseBody>> {
        return parkingSpotAPI.deleteParkingSpots()
            .toObservable()
    }

    fun deleteParkingSpot(id: String): Observable<Response<ResponseBody>> {
        return parkingSpotAPI.deleteParkingSpotDetail(id)
            .toObservable()
    }
}