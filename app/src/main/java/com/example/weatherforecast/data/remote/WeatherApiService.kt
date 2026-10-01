package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.remote.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current")
        current: String =
            "temperature_2m,relative_humidity_2m,weather_code",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}