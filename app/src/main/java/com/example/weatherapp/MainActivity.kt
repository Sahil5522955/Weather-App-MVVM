package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.provider.Settings
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.ScreenState
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

var cityName:String="Gurgaon"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val day= Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

        Handler(Looper.getMainLooper()).postDelayed({viewModel.weatherResponse.observe(this) { weather ->
            binding.apply {
                weather.state?.apply {
                    when (this) {
                        ScreenState.Loading -> {
                            pgBar.visibility = View.VISIBLE
                            mainScreen.visibility = View.GONE
                            errorScreen.visibility = View.GONE
                        }
                        ScreenState.Error -> {
                            pgBar.visibility = View.GONE
                            mainScreen.visibility = View.GONE
                            errorScreen.visibility = View.VISIBLE
                            btn.setOnClickListener {
                                finish()
                                startActivity(intent)
                            }
                        }
                        ScreenState.Success -> {
                            tvCity.text= cityName
                            pgBar.visibility = View.GONE
                            errorScreen.visibility = View.GONE
                            mainScreen.visibility = View.VISIBLE
                        }
                    }
                }

                weather.data?.apply {
                    startActivity(intent)
                    tvCity.text = cityName
                    val list=this.list
                    tvTemperature.text = "${list[0].main.temp.toInt()-273}°C"
                    Log.d("tag", "https://goweather.herokuapp.com/weather/Curitiba")


                    day1.text = dayName((day)%7)
                    day2.text = dayName((day + 1)%7)
                    day3.text = dayName((day + 2)%7)
                    day4.text = dayName((day + 3)%7)
                    tvForecast1.text = "${list[8].main.temp.toInt()-273}°C"
                    tvForecast2.text = "${list[16].main.temp.toInt()-273}°C"
                    tvForecast3.text = "${list[24].main.temp.toInt()-273}°C"
                    tvForecast4.text = "${list[32].main.temp.toInt()-273}°C"
                }

            }
        }},100)

    }
    private fun getCurrentLocation(){
        if(checkPermissions())
        {
            if(isLocationEnabled()){
                //final latitude and longitude
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                // fusedLocationClient.getCurrentLocation()
                fusedLocationClient.lastLocation.addOnCompleteListener(this){ task->
                    val location:Location?=task.result
                    if(location==null)
                    {
                        Toast.makeText(this,"NULL Received",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,"Location SUCCESS",Toast.LENGTH_SHORT).show()
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses: MutableList<android.location.Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        cityName = addresses!![0].locality
                        startActivity(intent)
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Turn on location",Toast.LENGTH_SHORT).show()
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else
        {
            //request permission here
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }
    private fun checkPermissions():Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Granted",Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else{
                Toast.makeText(applicationContext,"DENIED",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}
fun dayName(value:Int):String=when(value){
    0->"Sunday"
    1->"Monday"
    2->"Tuesday"
    3->"Wednesday"
    4->"Thursday"
    5->"Friday"
    6->"Saturday"
    else ->"Sunday"
}