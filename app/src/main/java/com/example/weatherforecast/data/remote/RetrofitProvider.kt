package com.example.weatherforecast.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val GEOCODING_BASE_URL =
        "https://geocoding-api.open-meteo.com/"

    private const val WEATHER_BASE_URL =
        "https://api.open-meteo.com/"

    private val geocodingRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEOCODING_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val weatherRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val geocodingApi: GeocodingApiService by lazy {
        geocodingRetrofit.create(GeocodingApiService::class.java)
    }

    val weatherApi: WeatherApiService by lazy {
        weatherRetrofit.create(WeatherApiService::class.java)
    }
}