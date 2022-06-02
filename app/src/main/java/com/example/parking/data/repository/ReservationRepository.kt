package com.example.parking.data.repository

import com.example.parking.data.network.modelJSON.ReservationJson
import com.example.parking.data.network.api.ReservationAPI
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class ReservationRepository(private val retrofit: Retrofit) {

    private val reservationAPI: ReservationAPI = retrofit.create(ReservationAPI::class.java)

    
    fun getReservation(id: String): Observable<Response<ReservationJson>> {
        return reservationAPI.getReservationDetail(id)
            .toObservable()
    }

    fun getAll(): Observable<Response<List<ReservationJson>>> {
        return reservationAPI.getReservations()
            .toObservable()
    }

    fun createReservation(carId: String,
                          employeeId: String,
                          parkingSpotId: String,
                          startTime: String,
                          endTime: String
    ): Observable<Response<ReservationJson>> {
        return reservationAPI.createReservation(
            hashMapOf(
                "carId" to carId,
                "employeeId" to employeeId,
                "parkingSpotId" to parkingSpotId,
                "startTime" to startTime,
                "endTime" to endTime
            )
        )
            .toObservable()
    }

    fun updateReservation(id: String,
                          carId: String,
                          employeeId: String,
                          parkingSpotId: String,
                          startTime: String,
                          endTime: String
    ): Observable<Response<ReservationJson>> {
        return reservationAPI.updateReservationDetail(
            id,
            hashMapOf(
                "id" to id,
                "carId" to carId,
                "employeeId" to employeeId,
                "parkingSpotId" to parkingSpotId,
                "startTime" to startTime,
                "endTime" to endTime
            )
        )
            .toObservable()
    }

    fun deleteAll(): Observable<Response<ResponseBody>> {
        return reservationAPI.deleteReservations()
            .toObservable()
    }

    fun deleteReservation(id: String): Observable<Response<ResponseBody>> {
        return reservationAPI.deleteReservationDetail(id)
            .toObservable()
    }
}