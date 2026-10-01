package com.example.weatherforecast

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.example.weatherforecast.data.WeatherRepository
import com.example.weatherforecast.data.WeatherResult
import com.example.weatherforecast.ui.WeatherUiState
import com.example.weatherforecast.ui.WeatherViewModel
import com.example.weatherforecast.ui.WeatherViewModelFactory
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var cityInputLayout: TextInputLayout
    private lateinit var cityEditText: TextInputEditText
    private lateinit var searchButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var errorText: TextView

    private lateinit var weatherCard: MaterialCardView
    private lateinit var locationText: TextView
    private lateinit var weatherIconText: TextView
    private lateinit var conditionText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var humidityText: TextView

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        initializeViews()
        initializeViewModel()
        setupSearch()
        observeWeatherState()
    }

    private fun initializeViews() {

        cityInputLayout = findViewById(R.id.cityInputLayout)
        cityEditText = findViewById(R.id.cityEditText)
        searchButton = findViewById(R.id.searchButton)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        statusText = findViewById(R.id.statusText)
        errorText = findViewById(R.id.errorText)

        weatherCard = findViewById(R.id.weatherCard)
        locationText = findViewById(R.id.locationText)
        weatherIconText = findViewById(R.id.weatherIconText)
        conditionText = findViewById(R.id.conditionText)
        temperatureText = findViewById(R.id.temperatureText)
        humidityText = findViewById(R.id.humidityText)
    }

    private fun initializeViewModel() {

        val repository = WeatherRepository()

        val factory = WeatherViewModelFactory(repository)

        weatherViewModel = ViewModelProvider(
            this,
            factory
        )[WeatherViewModel::class.java]
    }

    private fun setupSearch() {

        searchButton.setOnClickListener {
            searchWeather()
        }

        cityEditText.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchWeather()
                true
            } else {
                false
            }
        }
    }

    private fun searchWeather() {

        val cityName = cityEditText.text?.toString().orEmpty()

        hideKeyboard()

        weatherViewModel.searchCity(cityName)
    }

    private fun observeWeatherState() {

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                weatherViewModel.uiState.collect { state ->

                    when (state) {

                        WeatherUiState.Idle -> {
                            showIdleState()
                        }

                        WeatherUiState.Loading -> {
                            showLoadingState()
                        }

                        is WeatherUiState.Success -> {
                            showSuccessState(state.weather)
                        }

                        is WeatherUiState.Error -> {
                            showErrorState(state.message)
                        }
                    }
                }
            }
        }
    }

    private fun showIdleState() {

        loadingProgressBar.visibility = View.GONE
        weatherCard.visibility = View.GONE
        errorText.visibility = View.GONE

        searchButton.isEnabled = true
        cityEditText.isEnabled = true

        statusText.visibility = View.VISIBLE
        statusText.text =
            "Enter a city name to get the current weather."

        cityInputLayout.error = null
    }

    private fun showLoadingState() {

        loadingProgressBar.visibility = View.VISIBLE
        weatherCard.visibility = View.GONE
        errorText.visibility = View.GONE

        searchButton.isEnabled = false
        cityEditText.isEnabled = false

        statusText.visibility = View.VISIBLE
        statusText.text =
            "Loading weather information..."

        cityInputLayout.error = null
    }

    private fun showSuccessState(weather: WeatherResult) {

        loadingProgressBar.visibility = View.GONE
        errorText.visibility = View.GONE

        searchButton.isEnabled = true
        cityEditText.isEnabled = true

        statusText.visibility = View.VISIBLE
        statusText.text = "Current weather"

        weatherCard.visibility = View.VISIBLE

        locationText.text =
            buildLocationText(weather)

        conditionText.text =
            weatherCodeToDescription(weather.weatherCode)

        weatherIconText.text =
            weatherCodeToIcon(weather.weatherCode)

        temperatureText.text =
            "${formatTemperature(weather.temperature)}°C"

        humidityText.text =
            "${weather.humidity}%"

        cityInputLayout.error = null
    }

    private fun showErrorState(message: String) {

        loadingProgressBar.visibility = View.GONE
        weatherCard.visibility = View.GONE

        searchButton.isEnabled = true
        cityEditText.isEnabled = true

        statusText.visibility = View.GONE

        errorText.visibility = View.VISIBLE
        errorText.text = message

        cityInputLayout.error = null
    }

    private fun buildLocationText(weather: WeatherResult): String {

        return if (weather.country.isNullOrBlank()) {
            weather.cityName
        } else {
            "${weather.cityName}, ${weather.country}"
        }
    }

    private fun formatTemperature(temperature: Double): String {

        return if (temperature == temperature.toInt().toDouble()) {
            temperature.toInt().toString()
        } else {
            String.format(
                Locale.US,
                "%.1f",
                temperature
            )
        }
    }

    private fun weatherCodeToDescription(code: Int): String {

        return when (code) {

            0 -> "Clear sky"

            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"

            45 -> "Fog"
            48 -> "Depositing rime fog"

            51 -> "Light drizzle"
            53 -> "Moderate drizzle"
            55 -> "Dense drizzle"

            56 -> "Light freezing drizzle"
            57 -> "Dense freezing drizzle"

            61 -> "Slight rain"
            63 -> "Moderate rain"
            65 -> "Heavy rain"

            66 -> "Light freezing rain"
            67 -> "Heavy freezing rain"

            71 -> "Slight snowfall"
            73 -> "Moderate snowfall"
            75 -> "Heavy snowfall"

            77 -> "Snow grains"

            80 -> "Slight rain showers"
            81 -> "Moderate rain showers"
            82 -> "Violent rain showers"

            85 -> "Slight snow showers"
            86 -> "Heavy snow showers"

            95 -> "Thunderstorm"

            96 -> "Thunderstorm with slight hail"
            97 -> "Heavy thunderstorm"
            99 -> "Thunderstorm with heavy hail"

            else -> "Unknown weather condition"
        }
    }

    private fun weatherCodeToIcon(code: Int): String {

        return when (code) {

            0 -> "☀️"

            1 -> "🌤️"
            2 -> "⛅"
            3 -> "☁️"

            45, 48 -> "🌫️"

            51, 53, 55,
            56, 57,
            61, 63, 65,
            66, 67,
            80, 81, 82 -> "🌧️"

            71, 73, 75,
            77, 85, 86 -> "❄️"

            95, 96, 97, 99 -> "⛈️"

            else -> "🌡️"
        }
    }

    private fun hideKeyboard() {

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(
            cityEditText.windowToken,
            0
        )
    }
}