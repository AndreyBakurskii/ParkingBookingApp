package com.example.parking.data.repository

import android.util.Log
import com.example.parking.data.network.modelJSON.ReservationJson
import com.example.parking.data.network.api.ReservationAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class ReservationRepository(private val retrofitAdminAuth: Retrofit, private val retrofitUserAuth: Retrofit) {

    private val apiAdminAuth: ReservationAPI = retrofitAdminAuth.create(ReservationAPI::class.java)
    private val apiUserAuth: ReservationAPI = retrofitUserAuth.create(ReservationAPI::class.java)

    private var currentAuthRole: String = "admin"
    private var currentApi: ReservationAPI = apiAdminAuth

    fun getCurrentAuthRole(): String {
        return currentAuthRole
    }

    fun setCurrentAuthRole(newAuthRole: String) {
        currentAuthRole = newAuthRole
        currentApi = if (currentAuthRole == "admin") apiAdminAuth else apiUserAuth
    }

    fun getReservation(id: String): Triple<ReservationJson?, Int, String?> {
        var result: Triple<ReservationJson?, Int, String?> = Triple(null, -1, null)

        currentApi.getReservationDetail(id).enqueue(object : Callback<ReservationJson> {
            override fun onResponse(call: Call<ReservationJson>, response: Response<ReservationJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<ReservationJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun getAll(): Triple<MutableList<ReservationJson>?, Int, String?> {
        var result: Triple<MutableList<ReservationJson>?, Int, String?> = Triple(null, -1, null)

        currentApi.getReservations().enqueue(object : Callback<MutableList<ReservationJson>> {
            override fun onResponse(call: Call<MutableList<ReservationJson>>, response: Response<MutableList<ReservationJson>>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                    Log.i("Result", result.toString())

                }
                else {
                    // TODO: implement if fail status code
                    result = Triple(response.body(), response.code(), null)
                    Log.i("Result", result.toString())
                }
            }
            override fun onFailure(call: Call<MutableList<ReservationJson>>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun createReservation(carId: String,
                          employeeId: String,
                          parkingSpotId: String,
                          startTime: String,
                          endTime: String
    ): Triple<ReservationJson?, Int, String?> {
        var result: Triple<ReservationJson?, Int, String?> = Triple(null, -1, null)

        var reservation: HashMap<String, Any> = hashMapOf(
            "carId" to carId,
            "employeeId" to employeeId,
            "parkingSpotId" to parkingSpotId,
            "startTime" to startTime,
            "endTime" to endTime
        )

        currentApi.createReservation(reservation).enqueue(object : Callback<ReservationJson> {
            override fun onResponse(call: Call<ReservationJson>, response: Response<ReservationJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                    Log.i("Result", result.toString())
                }
                else {
                    // TODO: implement if fail status code
                    result = Triple(response.body(), response.code(), null)
                    Log.i("Result", result.toString())
                }
            }
            override fun onFailure(call: Call<ReservationJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun updateReservation(id: String,
                          carId: String,
                          employeeId: String,
                          parkingSpotId: String,
                          startTime: String,
                          endTime: String
    ): Triple<ReservationJson?, Int, String?> {
        var result: Triple<ReservationJson?, Int, String?> = Triple(null, -1, null)

        var reservation: HashMap<String, Any> = hashMapOf(
            "id" to id,
            "carId" to carId,
            "employeeId" to employeeId,
            "parkingSpotId" to parkingSpotId,
            "startTime" to startTime,
            "endTime" to endTime
        )

        currentApi.updateReservationDetail(id, reservation).enqueue(object : Callback<ReservationJson> {
            override fun onResponse(call: Call<ReservationJson>, response: Response<ReservationJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<ReservationJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun deleteAll(): Triple<ReservationJson?, Int, String?> {
        var result: Triple<ReservationJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteReservations().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                if (response!!.isSuccessful) {
                    result = Triple(null, response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })
        
        return result
    }

    fun deleteReservation(id: String): Triple<ReservationJson?, Int, String?> {
        var result: Triple<ReservationJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteReservationDetail(id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                if (response!!.isSuccessful) {
                    result = Triple(null, response.code(), null)
                    Log.i("RESULT", result.toString())
                }
                else {
                    // TODO: implement if fail status code
                    result = Triple(null, response.code(), null)
                    Log.i("RESULT", result.toString())
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("RESULT", result.toString())
            }
        })

        return result
    }
}