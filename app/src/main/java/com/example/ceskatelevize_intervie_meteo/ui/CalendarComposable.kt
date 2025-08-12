package com.example.ceskatelevize_intervie_meteo.ui

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarComposable(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    colors: DatePickerColors = DatePickerDefaults.colors(),
) {
    DatePicker(
        state = state,
        modifier = modifier,
        colors = colors,
        showModeToggle = false,
        headline = null,
        title = null,
    )
}