package com.example.ceskatelevize_intervie_meteo.datasource.remote

import com.example.ceskatelevize_intervie_meteo.Extensions
import com.example.ceskatelevize_intervie_meteo.datasource.OpenMeteoArchiveApiService
import com.example.ceskatelevize_intervie_meteo.datasource.model.MinMaxTemperature
import com.example.ceskatelevize_intervie_meteo.datasource.model.WeatherForecastData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.ZoneId

class MeteoApiRemoteDataSourceImpl(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: OpenMeteoArchiveApiService,
) : WeatherForecastRemoteDataSource {
    override suspend fun fetchTemperatureForecast(
        latitude: Double,
        longitude: Double,
    ): WeatherForecastData {
        return withContext(dispatcher) {
            val zoneId = ZoneId.systemDefault()
            val apiResponse = requireNotNull(apiService.getWeatherForecast(
                latitude = latitude,
                longitude = longitude,
                timezone = zoneId.id,
            ).body())
            WeatherForecastData(
                with(apiResponse) {
                    requireNotNull(daily.time)
                    daily.time.mapIndexed { index, timestamp ->
                        val minTemp = requireNotNull(daily.temperature2mMin.getOrNull(index))
                        val maxTemp = requireNotNull(daily.temperature2mMax.getOrNull(index))
                        Pair(
                            Extensions.fromSecondsGetLocalDate(timestamp),
                            MinMaxTemperature(minTemp, maxTemp)
                        )
                    }.associateBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
                }
            )
        }
    }
}
