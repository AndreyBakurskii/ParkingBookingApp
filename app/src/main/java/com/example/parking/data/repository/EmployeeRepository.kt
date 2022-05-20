package com.example.parking.data.repository

import android.util.Log
import com.example.parking.data.network.modelJSON.EmployeeJson
import com.example.parking.data.network.api.EmployeeAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EmployeeRepository(private val retrofitAdminAuth: Retrofit, private val retrofitUserAuth: Retrofit) {

    private val apiAdminAuth: EmployeeAPI = retrofitAdminAuth.create(EmployeeAPI::class.java)
    private val apiUserAuth: EmployeeAPI = retrofitUserAuth.create(EmployeeAPI::class.java)

    private var currentAuthRole: String = "admin"
    private var currentApi: EmployeeAPI = apiAdminAuth

    fun getCurrentAuthRole(): String {
        return currentAuthRole
    }

    fun setCurrentAuthRole(newAuthRole: String) {
        currentAuthRole = newAuthRole
        currentApi = if (currentAuthRole == "admin") apiAdminAuth else apiUserAuth
    }

    fun getEmployee(id: String): Triple<EmployeeJson?, Int, String?> {
        var result: Triple<EmployeeJson?, Int, String?> = Triple(null, -1, null)

        currentApi.getEmployeeDetail(id).enqueue(object : Callback<EmployeeJson> {
            override fun onResponse(call: Call<EmployeeJson>, response: Response<EmployeeJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<EmployeeJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun getAll(): Triple<MutableList<EmployeeJson>?, Int, String?> {
        var result: Triple<MutableList<EmployeeJson>?, Int, String?> = Triple(null, -1, null)

        currentApi.getEmployees().enqueue(object : Callback<MutableList<EmployeeJson>> {
            override fun onResponse(call: Call<MutableList<EmployeeJson>>, response: Response<MutableList<EmployeeJson>>?) {
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
            override fun onFailure(call: Call<MutableList<EmployeeJson>>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun createEmployee(name: String): Triple<EmployeeJson?, Int, String?> {
        var result: Triple<EmployeeJson?, Int, String?> = Triple(null, -1, null)

        var id: String = ""  // TODO create UID for Employee
        var employee: HashMap<String, Any> = hashMapOf(
            "id" to id,
            "name" to name
        )

        currentApi.createEmployee(employee).enqueue(object : Callback<EmployeeJson> {
            override fun onResponse(call: Call<EmployeeJson>, response: Response<EmployeeJson>?) {
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
            override fun onFailure(call: Call<EmployeeJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
                Log.i("Result", result.toString())
            }
        })

        return result
    }

    fun updateEmployee(id: String, name: String): Triple<EmployeeJson?, Int, String?> {
        var result: Triple<EmployeeJson?, Int, String?> = Triple(null, -1, null)

        var employee: HashMap<String, Any> = hashMapOf(
            "id" to id,
            "name" to name
        )

        currentApi.updateEmployeeDetail(id, employee).enqueue(object : Callback<EmployeeJson> {
            override fun onResponse(call: Call<EmployeeJson>, response: Response<EmployeeJson>?) {
                if (response!!.isSuccessful) {
                    result = Triple(response.body(), response.code(), null)
                }
                else {
                    // TODO: implement if fail status code
                }
            }
            override fun onFailure(call: Call<EmployeeJson>, t: Throwable) {
                // there is more than just a failing request (like: no internet connection)
                result = Triple(null, 0, t.message)
            }
        })

        return result
    }

    fun deleteAll(): Triple<EmployeeJson?, Int, String?> {
        var result: Triple<EmployeeJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteEmployees().enqueue(object : Callback<ResponseBody> {
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

    fun deleteEmployee(id: String): Triple<EmployeeJson?, Int, String?> {
        var result: Triple<EmployeeJson?, Int, String?> = Triple(null, -1, null)

        currentApi.deleteEmployeeDetail(id).enqueue(object : Callback<ResponseBody> {
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
}