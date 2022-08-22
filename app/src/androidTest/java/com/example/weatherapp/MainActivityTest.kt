package com.example.weatherappq

import androidx.core.content.MimeTypeFilter.matches
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.http.GET

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest
{

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun beforeTest() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun launchedActivity() {
        withId(R.id.tvCity).matches(isDisplayed())
    }
    @Test
    fun launchedActivity2() {
        assert(5 == 5)
    }

    @Test
    fun checkingVisibility(){
        withId(R.id.layout_mainActivity).matches(isDisplayed())
    }
    @Test
    fun temperatureVisibility(){
        withId(R.id.tvTemperature).matches(isDisplayed())
    }
    @Test
    fun locationVisibility(){
        withId(R.id.tvCity).matches(isDisplayed())
    }
    @Test
    fun forecastTempVisibility(){
        withId(R.id.tvForecast1).matches(isDisplayed())
    }
}