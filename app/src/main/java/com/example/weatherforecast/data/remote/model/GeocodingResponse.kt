package com.example.weatherforecast.data.remote.model

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @SerializedName("results")
    val results: List<GeocodingLocation>? = null
)

data class GeocodingLocation(
    @SerializedName("name")
    val name: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("country")
    val country: String? = null
)