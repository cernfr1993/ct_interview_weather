package com.example.ceskatelevize_intervie_meteo.repository

import com.example.ceskatelevize_intervie_meteo.datasource.local.WeatherForecastLocalDataSource
import com.example.ceskatelevize_intervie_meteo.datasource.model.MinMaxTemperatureWithAvailableDates
import com.example.ceskatelevize_intervie_meteo.datasource.remote.WeatherForecastRemoteDataSource
import java.time.LocalDate

class WeatherRepositoryImpl(
    val local: WeatherForecastLocalDataSource,
    val remote: WeatherForecastRemoteDataSource,
): WeatherRepository {
    override suspend fun getWeatherMinMaxTemperatures(
        latitude: Double,
        longitude: Double,
        date: LocalDate,
        refreshData: Boolean,
    ): Result<MinMaxTemperatureWithAvailableDates> {
        return runCatching {
            val weatherForecast = if (refreshData) { // City has changed -> Fetching new data
                remote.fetchTemperatureForecast(latitude, longitude).also {
                    local.forecastData = it
                }
            } else {
                local.forecastData ?: remote.fetchTemperatureForecast(latitude, longitude).also {
                    local.forecastData = it
                }
            }
            val minMaxTemperature = weatherForecast.minMaxTemperatures[date]
            if(minMaxTemperature == null) {
                throw IllegalArgumentException("Min / Max temperature not available for this date.")
            }
            MinMaxTemperatureWithAvailableDates(
                weatherForecast.minMaxTemperatures.keys,
                minMaxTemperature,
            )
        }
    }
}
