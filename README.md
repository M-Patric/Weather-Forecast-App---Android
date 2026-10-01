# Weather Forecast App

A beginner-friendly Android weather application that allows users to search for a city and view its current weather information.

This project was developed as part of the Auspify Technologies Android Developer Internship Task 3 — Weather Forecast App.

## Project Overview

The Weather Forecast App retrieves weather information based on a city name entered by the user.

The application:

- Accepts a city name as input
- Resolves the city to geographic coordinates
- Retrieves current weather information
- Displays temperature
- Displays humidity
- Displays the current weather condition
- Displays a corresponding weather icon
- Provides loading feedback
- Handles invalid cities
- Handles network errors
- Supports searching using the keyboard

## Internship Task Requirements

The official Task 3 requirements include:

- Design a clean weather dashboard
- Integrate a weather API
- Fetch weather data based on city name
- Display temperature, humidity, and weather conditions
- Handle API errors and loading states

The implementation in this project addresses these requirements.

## Features

### City Search

Users can enter a city name and request the current weather information.

### Current Weather

The application retrieves current weather information for the selected city.

### Weather Details

The dashboard displays:

- City and country
- Temperature in Celsius
- Humidity
- Weather condition
- Weather icon

### Loading State

While weather information is being retrieved, the application displays a loading indicator and temporarily disables the search controls.

### Error Handling

The application handles:

- Empty city input
- Cities that cannot be found
- Network connection failures
- Unexpected data or application errors

### Keyboard Search

Users can submit a search directly from the keyboard using the search action.

## Technologies Used

- Kotlin
- Android Studio
- Android Views
- Material Design Components
- Retrofit
- Gson
- Kotlin Coroutines
- StateFlow
- ViewModel
- Repository pattern
- REST APIs

## Architecture

The application uses a layered architecture inspired by the MVVM pattern.

```text
User Interface
      ↓
MainActivity
      ↓
WeatherViewModel
      ↓
WeatherRepository
      ↓
Retrofit API Services
      ↓
Open-Meteo APIs

