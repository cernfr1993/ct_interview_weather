package com.example.ceskatelevize_intervie_meteo.datasource

import com.example.ceskatelevize_intervie_meteo.datasource.model.OpenMeteoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoArchiveApiService {

    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") dailyParameters: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh",
        @Query("precipitation_unit") precipitationUnit: String = "mm",
        @Query("timezone") timezone: String = "Europe/Iceland", // URL encoded by Retrofit
        @Query("timeformat") timeFormat: String = "unixtime",
        @Query("forecast_days") forecastDays: Int = 16
    ): Response<OpenMeteoResponse>
}
