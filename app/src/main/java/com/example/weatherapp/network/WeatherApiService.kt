package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast1
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeather(@Query("q") q:String, @Query("appid")appid:String):Response<Forecast1>
}