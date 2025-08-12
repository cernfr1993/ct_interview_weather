package com.example.ceskatelevize_intervie_meteo.repository

import com.example.ceskatelevize_intervie_meteo.datasource.model.MinMaxTemperatureWithAvailableDates
import java.time.LocalDate

interface WeatherRepository {
    suspend fun getWeatherMinMaxTemperatures (
        latitude: Double,
        longitude: Double,
        date: LocalDate,
        refreshData: Boolean = false,
    ): Result<MinMaxTemperatureWithAvailableDates>
}
