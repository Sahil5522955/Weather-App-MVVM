package com.example.weatherapp.viewmodel

import com.example.weatherapp.model.Forecast1

enum class ScreenState {
    Success,
    Error,
    Loading
}

data class WeatherScreenState(
    val data: Forecast1? = null,
    val state: ScreenState
)