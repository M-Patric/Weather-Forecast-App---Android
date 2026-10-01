package com.example.weatherforecast.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather? = null
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature2m: Double,

    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: Int,

    @SerializedName("weather_code")
    val weatherCode: Int
)