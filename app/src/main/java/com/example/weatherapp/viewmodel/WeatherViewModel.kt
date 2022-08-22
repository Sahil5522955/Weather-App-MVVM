package com.example.weatherapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.MyApplication
import com.example.weatherapp.cityName
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(app:Application,private val repository: WeatherRepository): AndroidViewModel(app){
    private val _response= MutableLiveData<WeatherScreenState>()
    // private val _status=_response
    val weatherResponse:LiveData<WeatherScreenState>
        get() = _response

    init{
        getWeather()
    }

    private fun getWeather() = viewModelScope.launch {
        if (hasInternetConnection()){
            _response.value = WeatherScreenState(state = ScreenState.Loading)
            repository.getWeather(cityName,ID_API="1b7b9c277f98ab5ec8e03f1dba3cb4e0").let { response ->
                if (response.isSuccessful) {
                    _response.postValue(
                        WeatherScreenState(
                            state = ScreenState.Success,
                            data = response.body()
                        )
                    )
                } else {
                    _response.value = WeatherScreenState(state = ScreenState.Error,data=response.body())
                    Log.d("Tag", "GetWeather Error : ${response.message()}")
                }
            }}
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            val activeNetwork=connectivityManager.activeNetwork?:return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI)->true
                capabilities.hasTransport(TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(TRANSPORT_ETHERNET)->true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI->true
                    TYPE_MOBILE->true
                    TYPE_ETHERNET->true
                    else ->false
                }
            }
        }
        return false
    }
}