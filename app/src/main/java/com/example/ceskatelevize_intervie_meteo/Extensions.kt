package com.example.ceskatelevize_intervie_meteo

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object Extensions {
    fun fromMillisGetLocalDate(milliseconds: Long): LocalDate {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun fromSecondsGetLocalDate(seconds: Long): LocalDate {
        return Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
