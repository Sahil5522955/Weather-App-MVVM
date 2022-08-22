package com.example.weatherapp.model

data class Forecast1(
    val cnt: Int,
    val cod: String,
    val list: List<Aux>,
    val message: Int
)