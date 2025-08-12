package com.example.ceskatelevize_intervie_meteo.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoResponse(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("generationtime_ms")
    val generationTimeMs: Double,
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerialName("elevation")
    val elevation: Double,
    @SerialName("daily_units")
    val dailyUnits: DailyUnits,
    @SerialName("daily")
    val daily: DailyData,
)

@Serializable
data class DailyUnits(
    @SerialName("time")
    val time: String,
    @SerialName("temperature_2m_max")
    val temperature2mMax: String,
    @SerialName("temperature_2m_min")
    val temperature2mMin: String
)

@Serializable
data class DailyData(
    @SerialName("time")
    val time: List<Long>?,
    @SerialName("temperature_2m_max")
    val temperature2mMax: List<Double?>, // Nullable in case data is missing
    @SerialName("temperature_2m_min")
    val temperature2mMin: List<Double?>  // Nullable
)
