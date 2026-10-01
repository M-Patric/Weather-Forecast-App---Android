package com.example.weatherforecast.data

import com.example.weatherforecast.data.remote.RetrofitProvider

data class WeatherResult(
    val cityName: String,
    val country: String?,
    val temperature: Double,
    val humidity: Int,
    val weatherCode: Int
)

class CityNotFoundException(cityName: String) :
    Exception("No location found for \"$cityName\".")

class WeatherDataException :
    Exception("Weather data is currently unavailable.")

class WeatherRepository {

    private val geocodingApi = RetrofitProvider.geocodingApi
    private val weatherApi = RetrofitProvider.weatherApi

    suspend fun getWeatherByCity(cityName: String): WeatherResult {

        val locations = geocodingApi.searchCity(cityName.trim()).results

        val location = locations?.firstOrNull()
            ?: throw CityNotFoundException(cityName)

        val weatherResponse = weatherApi.getCurrentWeather(
            latitude = location.latitude,
            longitude = location.longitude
        )

        val currentWeather = weatherResponse.current
            ?: throw WeatherDataException()

        return WeatherResult(
            cityName = location.name,
            country = location.country,
            temperature = currentWeather.temperature2m,
            humidity = currentWeather.relativeHumidity2m,
            weatherCode = currentWeather.weatherCode
        )
    }
}