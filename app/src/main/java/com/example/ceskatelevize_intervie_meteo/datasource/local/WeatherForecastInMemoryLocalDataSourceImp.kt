package com.example.ceskatelevize_intervie_meteo.datasource.local

import com.example.ceskatelevize_intervie_meteo.datasource.model.WeatherForecastData

class WeatherForecastInMemoryDataSource: WeatherForecastLocalDataSource {
    var weatherForecastData: WeatherForecastData? = null
    override var forecastData: WeatherForecastData?
        get() = weatherForecastData
        set(value) {
            weatherForecastData = value
        }
}