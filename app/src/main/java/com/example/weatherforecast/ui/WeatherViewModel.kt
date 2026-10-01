package com.example.weatherforecast.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.CityNotFoundException
import com.example.weatherforecast.data.WeatherDataException
import com.example.weatherforecast.data.WeatherRepository
import com.example.weatherforecast.data.WeatherResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface WeatherUiState {

    data object Idle : WeatherUiState

    data object Loading : WeatherUiState

    data class Success(
        val weather: WeatherResult
    ) : WeatherUiState

    data class Error(
        val message: String
    ) : WeatherUiState
}

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(
        WeatherUiState.Idle
    )

    val uiState: StateFlow<WeatherUiState> =
        _uiState.asStateFlow()

    fun searchCity(cityName: String) {

        val cleanedCityName = cityName.trim()

        if (cleanedCityName.isBlank()) {
            _uiState.value = WeatherUiState.Error(
                "Please enter a city name."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = WeatherUiState.Loading

            try {

                val weather = repository.getWeatherByCity(
                    cleanedCityName
                )

                _uiState.value = WeatherUiState.Success(
                    weather
                )

            } catch (exception: CityNotFoundException) {

                _uiState.value = WeatherUiState.Error(
                    exception.message
                        ?: "The city could not be found."
                )

            } catch (exception: WeatherDataException) {

                _uiState.value = WeatherUiState.Error(
                    exception.message
                        ?: "Weather data is unavailable."
                )

            } catch (exception: IOException) {

                _uiState.value = WeatherUiState.Error(
                    "Please check your internet connection."
                )

            } catch (exception: CancellationException) {

                throw exception

            } catch (exception: Exception) {

                _uiState.value = WeatherUiState.Error(
                    "Something went wrong. Please try again."
                )
            }
        }
    }
}