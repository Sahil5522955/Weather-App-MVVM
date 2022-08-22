package com.example.weatherapp.repository

import com.example.weatherapp.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository
@Inject
constructor(private val apiService: WeatherApiService){
    suspend fun getWeather(cityName:String,ID_API:String)=apiService.getWeather(cityName,ID_API)
}