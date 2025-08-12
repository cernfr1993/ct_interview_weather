package com.example.ceskatelevize_intervie_meteo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceskatelevize_intervie_meteo.Event
import com.example.ceskatelevize_intervie_meteo.datasource.model.MinMaxTemperature
import com.example.ceskatelevize_intervie_meteo.model.AvailableCity
import com.example.ceskatelevize_intervie_meteo.model.Location
import com.example.ceskatelevize_intervie_meteo.repository.LocationRepository
import com.example.ceskatelevize_intervie_meteo.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class MainUiState(
    val minMaxTemperature: MinMaxTemperature? = null,
    val selectedDate: LocalDate? = null,
    val selectedCity: AvailableCity? = null,
    val availableDates: List<LocalDate>? = null,
    val errorMessage: Event<String>? = null,
    val gpsLocation: Location? = null,
    val loading: Boolean? = null,
    val searchingLocation: Boolean? = null,
)

sealed interface MainStateEvent {
    data class DateChanged(val date: LocalDate?) : MainStateEvent
    data class CityChanged(val city: AvailableCity) : MainStateEvent
    data object LocationRequested : MainStateEvent
}

class MainViewModel(
    private val weatherRepository: WeatherRepository, // Injected by Koin
    private val locationRepository: LocationRepository, // Injected by Koin
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState(loading = true))
    val uiState = _uiState.asStateFlow()

    fun processEvent(event: MainStateEvent) {
        when (event) {
            is MainStateEvent.CityChanged -> {
                getTemperaturesCityChange(event.city)
            }
            is MainStateEvent.DateChanged -> {
                if (event.date != _uiState.value.selectedDate) {    // Do not call API again if date is the same. (Rotation).
                    getTemperatureDateChange(event.date ?: LocalDate.now())
                }
            }
            MainStateEvent.LocationRequested -> {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        viewModelScope.launch {
            _uiState.update { mainUiState ->
                mainUiState.copy(
                    searchingLocation = true
                )
            }
            val location = locationRepository.getCurrentLocation()
            if (location != null) {
                val temperatureWithDates = weatherRepository.getWeatherMinMaxTemperatures(
                    location.latitude,
                    location.longitude,
                    uiState.value.selectedDate ?: LocalDate.now(),
                    refreshData = true
                )
                _uiState.update { uiState.value.copy(
                    searchingLocation = false,
                    gpsLocation = location,
                    minMaxTemperature = temperatureWithDates.getOrNull()?.temperature,
                    errorMessage = temperatureWithDates.exceptionOrNull()?.let { Event(it.message.orEmpty()) }                ) }
            } else {
                _uiState.update { uiState.value.copy(
                    searchingLocation = false,
                    gpsLocation = null,
                    errorMessage = Event("Unable to fetch the GPS location.")
                ) }
            }
        }
    }

    private fun getTemperatureDateChange(date: LocalDate) {
        viewModelScope.launch {
            val city = uiState.value.selectedCity ?: AvailableCity.BRNO
            val temperatureWithDates = weatherRepository.getWeatherMinMaxTemperatures(
                city.latitude,
                city.longitude,
                date,
            )
            _uiState.update { mainUiState ->
                mainUiState.copy(
                    selectedDate = date,
                    availableDates = temperatureWithDates.getOrNull()?.availableDates?.toList(),
                    minMaxTemperature = temperatureWithDates.getOrNull()?.temperature,
                    errorMessage = temperatureWithDates.exceptionOrNull()?.let { Event(it.message.orEmpty()) }
                )
            }
        }
    }

    private fun getTemperaturesCityChange(city: AvailableCity) {
        viewModelScope.launch {
            val temperatureWithDates = weatherRepository.getWeatherMinMaxTemperatures(
                city.latitude,
                city.longitude,
                uiState.value.selectedDate ?: LocalDate.now(),
                refreshData = true,
            )
            _uiState.update { mainUiState ->
                mainUiState.copy(
                    availableDates = temperatureWithDates.getOrNull()?.availableDates?.toList(),
                    selectedCity = city,
                    minMaxTemperature = temperatureWithDates.getOrNull()?.temperature,
                    errorMessage = temperatureWithDates.exceptionOrNull()?.let { Event(it.message.orEmpty()) }                )
            }
        }
    }
}
