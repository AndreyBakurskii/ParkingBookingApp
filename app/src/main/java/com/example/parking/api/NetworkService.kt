package com.example.parking.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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


class NetworkService(private val authRole: String, private val authPassword: String) {
    private val BASE_URL = "https://parking-backend-140422.herokuapp.com"
    private val client = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(authRole, authPassword))
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}