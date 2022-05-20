package com.example.parking.data.repository

import android.util.Log
import com.example.parking.data.network.modelJSON.ParkingSpotJson
import com.example.parking.data.network.api.ParkingSpotAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class ParkingSpotRepository(private val retrofitAdminAuth: Retrofit, private val retrofitUserAuth: Retrofit) {

    private val apiAdminAuth: ParkingSpotAPI = retrofitAdminAuth.create(ParkingSpotAPI::class.java)
    private val apiUserAuth: ParkingSpotAPI = retrofitUserAuth.create(ParkingSpotAPI::class.java)

    private var currentAuthRole: String = "admin"
    private var currentApi: ParkingSpotAPI = apiAdminAuth

    fun getCurrentAuthRole(): String {
        return currentAuthRole
    }

    fun setCurrentAuthRole(newAuthRole: String) {
        currentAuthRole = newAuthRole
        currentApi = if (currentAuthRole == "admin") apiAdminAuth else apiUserAuth
    }
    
    fun getParkingSpot(id: String): Triple<ParkingSpotJson?, Int, String?> {
        var result: Triple<ParkingSpotJson?, Int, String?> = Triple(null, -1, null)

        currentApi.getParkingSpotDetail(id).enqueue(object : Callback<ParkingSpotJson> {
            override fun onResponse(call: Call<ParkingSpotJson>, response: Response<ParkingSpotJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<ParkingSpotJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun getAll(): Triple<MutableList<ParkingSpotJson>?, Int, String?> {
        var result: Triple<MutableList<ParkingSpotJson>?, Int, String?> = Triple(null, -1, null)

        currentApi.getParkingSpots().enqueue(object : Callback<MutableList<ParkingSpotJson>> {
            override fun onResponse(call: Call<MutableList<ParkingSpotJson>>, response: Response<MutableList<ParkingSpotJson>>?) {
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
            override fun onFailure(call: Call<MutableList<ParkingSpotJson>>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun createParkingSpot(parkingNumber: Int, isFree: Boolean): Triple<ParkingSpotJson?, Int, String?> {
        var result: Triple<ParkingSpotJson?, Int, String?> = Triple(null, -1, null)

        var parkingSpot: HashMap<String, Any> = hashMapOf(
            "parkingNumber" to parkingNumber,
            "isFree" to isFree,
        )

        currentApi.createParkingSpot(parkingSpot).enqueue(object : Callback<ParkingSpotJson> {
            override fun onResponse(call: Call<ParkingSpotJson>, response: Response<ParkingSpotJson>?) {
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
            override fun onFailure(call: Call<ParkingSpotJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun updateParkingSpot(id: String, parkingNumber: Int, isFree: Boolean): Triple<ParkingSpotJson?, Int, String?> {
        var result: Triple<ParkingSpotJson?, Int, String?> = Triple(null, -1, null)

        var parkingSpot: HashMap<String, Any> = hashMapOf(
            "id" to id,
            "parkingNumber" to parkingNumber,
            "isFree" to isFree
        )

        currentApi.updateParkingSpotDetail(id, parkingSpot).enqueue(object : Callback<ParkingSpotJson> {
            override fun onResponse(call: Call<ParkingSpotJson>, response: Response<ParkingSpotJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<ParkingSpotJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun deleteAll(): Triple<ParkingSpotJson?, Int, String?> {
        var result: Triple<ParkingSpotJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteParkingSpots().enqueue(object : Callback<ResponseBody> {
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

    fun deleteParkingSpot(id: String): Triple<ParkingSpotJson?, Int, String?> {
        var result: Triple<ParkingSpotJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteParkingSpotDetail(id).enqueue(object : Callback<ResponseBody> {
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