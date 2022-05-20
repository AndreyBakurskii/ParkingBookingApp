package com.example.parking.data.repository

import android.util.Log
import com.example.parking.data.network.modelJSON.CarJson
import com.example.parking.data.network.api.CarAPI
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

//class CarAPI(private val retrofitAdminAuth: Retrofit, private val retrofitUserAuth: Retrofit) {
class CarRepository(private val retrofitAdminAuth: Retrofit) {

    private val apiAdminAuth: CarAPI = retrofitAdminAuth.create(CarAPI::class.java)
//    private val apiUserAuth: CarAPI = retrofitUserAuth.create(CarAPI::class.java)

    private var currentAuthRole: String = "admin"
    private var currentApi: CarAPI = apiAdminAuth

    fun getCurrentAuthRole(): String {
        return currentAuthRole
    }

    fun setCurrentAuthRole(newAuthRole: String) {
        currentAuthRole = newAuthRole
//        currentApi = if (currentAuthRole == "admin") apiAdminAuth else apiUserAuth
    }

    fun getCar(id: String): Triple<CarJson?, Int, String?> {
        var result: Triple<CarJson?, Int, String?> = Triple(null, -1, null)

        currentApi.getCarDetail(id).enqueue(object : Callback<CarJson> {
            override fun onResponse(call: Call<CarJson>, response: Response<CarJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<CarJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun getAll(): Observable<List<CarJson>>{
        return currentApi.getCars()
//            .map { car ->  }
            .toObservable()
    }

    fun createCar(model: String, registryNumber: String, wight: Int, length: Int): Triple<CarJson?, Int, String?> {
        var result: Triple<CarJson?, Int, String?> = Triple(null, -1, null)

        var car: HashMap<String, Any> = hashMapOf(
            "model" to model,
            "registryNumber" to registryNumber,
            "wight" to wight,
            "length" to length
        )

        currentApi.createCar(car).enqueue(object : Callback<CarJson> {
            override fun onResponse(call: Call<CarJson>, response: Response<CarJson>?) {
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
            override fun onFailure(call: Call<CarJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun updateCar(id: String, model: String, registryNumber: String, wight: Int, length: Int): Triple<CarJson?, Int, String?> {
        var result: Triple<CarJson?, Int, String?> = Triple(null, -1, null)

        var car: HashMap<String, Any> = hashMapOf(
            "model" to model,
            "registryNumber" to registryNumber,
            "wight" to wight,
            "length" to length
        )

        currentApi.updateCarDetail(id, car).enqueue(object : Callback<CarJson> {
            override fun onResponse(call: Call<CarJson>, response: Response<CarJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<CarJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun deleteAll(): Triple<CarJson?, Int, String?> {
        var result: Triple<CarJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteCars().enqueue(object : Callback<ResponseBody> {
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

    fun deleteCar(id: String): Triple<CarJson?, Int, String?> {
        var result: Triple<CarJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteCarDetail(id).enqueue(object : Callback<ResponseBody> {
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