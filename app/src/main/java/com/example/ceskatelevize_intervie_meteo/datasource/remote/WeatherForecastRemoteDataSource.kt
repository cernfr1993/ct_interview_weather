package com.example.ceskatelevize_intervie_meteo.datasource.remote

import com.example.ceskatelevize_intervie_meteo.datasource.model.WeatherForecastData

interface WeatherForecastRemoteDataSource {
    suspend fun fetchTemperatureForecast(latitude: Double, longitude: Double): WeatherForecastData
}
