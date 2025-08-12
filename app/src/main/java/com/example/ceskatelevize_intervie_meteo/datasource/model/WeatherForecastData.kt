package com.example.ceskatelevize_intervie_meteo.datasource.model

import java.time.LocalDate

data class WeatherForecastData(
    val minMaxTemperatures: Map<LocalDate, MinMaxTemperature>,
)

data class MinMaxTemperatureWithAvailableDates(
    val availableDates: Set<LocalDate>,
    val temperature: MinMaxTemperature,
)

data class MinMaxTemperature(
    val minTemperature: Double,
    val maxTemperature: Double,
)
