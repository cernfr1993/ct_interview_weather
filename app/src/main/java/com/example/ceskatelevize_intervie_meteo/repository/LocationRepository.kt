package com.example.ceskatelevize_intervie_meteo.repository

import com.example.ceskatelevize_intervie_meteo.model.Location

interface LocationRepository {
    suspend fun  getCurrentLocation(): Location?
}
