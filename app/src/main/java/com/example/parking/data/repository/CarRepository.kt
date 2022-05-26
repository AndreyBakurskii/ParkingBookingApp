package com.example.parking.data.repository


import com.example.parking.data.network.modelJSON.CarJson
import com.example.parking.data.network.api.CarAPI
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class CarRepository(private val retrofit: Retrofit) {

    private val carAPI: CarAPI = retrofit.create(CarAPI::class.java)
    
    
    fun getCar(id: String): Observable<Response<CarJson>> {
        return carAPI.getCarDetail(id)
            .toObservable()
    }

    fun getAll(): Observable<Response<List<CarJson>>>{
        return carAPI.getCars()
            .toObservable()
    }

    fun createCar(car: HashMap<String, Any>): Observable<Response<CarJson>> {
        return carAPI.createCar(car)
            .toObservable()
    }

    fun updateCar(id: String, car: HashMap<String, Any>): Observable<Response<CarJson>> {
        return carAPI.updateCarDetail(
            id,
            car
        )
            .toObservable()
    }

    fun deleteAll(): Observable<Response<ResponseBody>> {
        return carAPI.deleteCars()
            .toObservable()
    }

    fun deleteCar(id: String): Observable<Response<ResponseBody>> {
        return carAPI.deleteCarDetail(id)
            .toObservable()
    }
}