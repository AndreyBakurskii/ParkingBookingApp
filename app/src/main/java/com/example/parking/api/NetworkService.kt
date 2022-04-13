package com.example.parking.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// https://medium.com/@thoolab/how-to-implement-retrofit-basic-authentication-in-android-kotlin-9f25abe34dda
class BasicAuthInterceptor(username: String, password: String): Interceptor {
    private var credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}


class NetworkService(var isAdmin: Boolean) {
    private val BASE_URL = ""
    private val ADMIN_USERNAME = "admin"
    private val USER_USERNAME = "username"
    private val PASSWORD = "password"

    //add BasicAuthInterceptor to OkHttp client
    val client =  OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(if (isAdmin) ADMIN_USERNAME else USER_USERNAME, PASSWORD))
        .build()

    // add OkHttp client to Retrofit instance
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NetworkService::class.java)
}