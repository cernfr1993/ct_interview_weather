package com.example.ceskatelevize_intervie_meteo.datasource.local

import com.example.ceskatelevize_intervie_meteo.datasource.model.WeatherForecastData

interface WeatherForecastLocalDataSource {
    var forecastData: WeatherForecastData?
}
