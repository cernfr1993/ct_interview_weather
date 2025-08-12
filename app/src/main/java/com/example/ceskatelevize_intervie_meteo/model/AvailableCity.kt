package com.example.ceskatelevize_intervie_meteo.model

enum class AvailableCity(
    val cityName: String,
    val latitude: Double,
    val longitude: Double
) {
    PRAHA(
        cityName = "Praha",
        latitude = 50.0755,
        longitude = 14.4378,
    ),
    BRNO(
        cityName = "Brno",
        latitude = 49.1951,
        longitude = 16.6068,
    ),
}