package com.example.ceskatelevize_intervie_meteo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ceskatelevize_intervie_meteo.model.AvailableCity
import com.example.ceskatelevize_intervie_meteo.model.TemperatureType
import com.example.ceskatelevize_intervie_meteo.ui.CalendarComposable
import com.example.ceskatelevize_intervie_meteo.ui.CityButton
import com.example.ceskatelevize_intervie_meteo.ui.TemperatureCard
import com.example.ceskatelevize_intervie_meteo.ui.theme.CeskaTelevizeIntervieMeteoTheme
import com.example.ceskatelevize_intervie_meteo.viewmodel.MainStateEvent
import com.example.ceskatelevize_intervie_meteo.viewmodel.MainUiState
import com.example.ceskatelevize_intervie_meteo.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel by viewModel()
            CeskaTelevizeIntervieMeteoTheme {
                MainScreen(
                    viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val loadingState = remember { state.loading }

    val selectableUtcMillisSet = remember(state.availableDates) {
        state.availableDates?.toSet() ?: emptySet()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Date().time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return selectableUtcMillisSet.contains(
                    Extensions.fromMillisGetLocalDate(
                        utcTimeMillis
                    )
                )
            }
        }
    )

    LaunchedEffect(datePickerState) {
        coroutineScope.launch {
            viewModel.processEvent(
                MainStateEvent.DateChanged(
                    Extensions.fromMillisGetLocalDate(
                        datePickerState.selectedDateMillis ?: 0L
                    )
                )
            )
        }
    }

    state.errorMessage?.getContentIfNotHandled()?.let {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if(loadingState == true) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val isLandscape by remember { derivedStateOf { this.constraints.maxWidth > this.constraints.maxHeight } }
            if (isLandscape) {
                LandscapeMode(
                    viewModel,
                    innerPadding,
                    datePickerState,
                    state,
                    locationPermissionState,
                    snackbarHostState,
                    coroutineScope,
                )
            }
            else {
                PortraitMode(
                    viewModel,
                    innerPadding,
                    datePickerState,
                    state,
                    locationPermissionState,
                    snackbarHostState,
                    coroutineScope,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PortraitMode(
    viewModel: MainViewModel,
    innerPadding: PaddingValues,
    datePickerState: DatePickerState,
    mainUiState: MainUiState,
    locationPermissionState: PermissionState,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(innerPadding)
    ) {
        CalendarComposable(
            state = datePickerState,
            modifier = Modifier
                .weight(1.0f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TemperatureCard(
                TemperatureType.MIN,
                mainUiState.minMaxTemperature?.minTemperature ?: 0.0,
                modifier = Modifier.weight(1.0f)
            )
            TemperatureCard(
                TemperatureType.MAX,
                mainUiState.minMaxTemperature?.maxTemperature ?: 0.0,
                modifier = Modifier.weight(1.0f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CityButton(
                AvailableCity.BRNO,
                { city ->
                    viewModel.processEvent(MainStateEvent.CityChanged(city))
                },
                modifier = Modifier.weight(1.0f)
            )
            CityButton(
                AvailableCity.PRAHA,
                { city ->
                    viewModel.processEvent(MainStateEvent.CityChanged(city))
                },
                modifier = Modifier.weight(1.0f)
            )
        }
        Button(
            shape = RoundedCornerShape(16.dp),
            enabled = mainUiState.searchingLocation?.not() ?: true,
            onClick = {
                if (locationPermissionState.status.isGranted) {
                    viewModel.processEvent(MainStateEvent.LocationRequested)
                } else if (locationPermissionState.status.shouldShowRationale) {
                    coroutineScope.launch {
                        val snackbarResult = snackbarHostState.showSnackbar(
                            "For more precise weather info please enable GPS location.",
                            "Enable",
                            withDismissAction = false,
                        )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            openAppSettings(context)
                        }
                    }
                } else {
                    locationPermissionState.launchPermissionRequest()
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(6.dp),
        ) {
            Text(
                text = if (mainUiState.searchingLocation == true) "Searching..." else "Current location",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LandscapeMode(
    viewModel: MainViewModel,
    innerPadding: PaddingValues,
    datePickerState: DatePickerState,
    mainUiState: MainUiState,
    locationPermissionState: PermissionState,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize() // Fill the whole screen
            .padding(innerPadding)
    ) {
        Box(modifier = Modifier.weight(1.5f)) {
            CalendarComposable(
                state = datePickerState,
                modifier = Modifier
                    .fillMaxHeight()
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TemperatureCard(
                    TemperatureType.MIN,
                    mainUiState.minMaxTemperature?.minTemperature ?: 0.0,
                    modifier = Modifier.weight(1.0f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TemperatureCard(
                    TemperatureType.MAX,
                    mainUiState.minMaxTemperature?.maxTemperature ?: 0.0,
                    modifier = Modifier.weight(1.0f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // City Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CityButton(
                    AvailableCity.BRNO,
                    { city ->
                        viewModel.processEvent(MainStateEvent.CityChanged(city))
                    },
                    modifier = Modifier.weight(1.0f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                CityButton(
                    AvailableCity.PRAHA,
                    { city ->
                        viewModel.processEvent(MainStateEvent.CityChanged(city))
                    },
                    modifier = Modifier.weight(1.0f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // GPS Location Button
            Button(
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    if (locationPermissionState.status.isGranted) {
                        viewModel.processEvent(MainStateEvent.LocationRequested)
                    } else if (locationPermissionState.status.shouldShowRationale) {
                        coroutineScope.launch {
                            val snackbarResult = snackbarHostState.showSnackbar(
                                "For more precise weather info please enable GPS location.",
                                "Enable",
                                withDismissAction = false,
                            )
                            if (snackbarResult == SnackbarResult.ActionPerformed) {
                                openAppSettings(context)
                            }
                        }
                    } else {
                        locationPermissionState.launchPermissionRequest()
                    }
                },
                enabled = mainUiState.searchingLocation?.not() ?: true,
                modifier = Modifier
                    .fillMaxWidth() // Full width of this column
                    .height(48.dp)
            ) {
                Text(
                    text = if (mainUiState.searchingLocation == true) "Searching..." else "Current location",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun openAppSettings(context: Context) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).also { intent ->
        context.startActivity(intent)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Box {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
}
