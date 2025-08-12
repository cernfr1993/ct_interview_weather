package com.example.ceskatelevize_intervie_meteo.di

import com.example.ceskatelevize_intervie_meteo.datasource.OpenMeteoArchiveApiService
import com.example.ceskatelevize_intervie_meteo.datasource.local.WeatherForecastInMemoryDataSource
import com.example.ceskatelevize_intervie_meteo.datasource.local.WeatherForecastLocalDataSource
import com.example.ceskatelevize_intervie_meteo.datasource.remote.MeteoApiRemoteDataSourceImpl
import com.example.ceskatelevize_intervie_meteo.datasource.remote.WeatherForecastRemoteDataSource
import com.example.ceskatelevize_intervie_meteo.repository.LocationRepository
import com.example.ceskatelevize_intervie_meteo.repository.LocationRepositoryImpl
import com.example.ceskatelevize_intervie_meteo.repository.WeatherRepository
import com.example.ceskatelevize_intervie_meteo.repository.WeatherRepositoryImpl
import com.example.ceskatelevize_intervie_meteo.viewmodel.MainViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.open-meteo.com"

val mainModule = module {
    single<WeatherForecastRemoteDataSource> {
        MeteoApiRemoteDataSourceImpl(
            apiService = get(),
            dispatcher = Dispatchers.IO
        )
    }
    single<WeatherForecastLocalDataSource> {
        WeatherForecastInMemoryDataSource()
    }

    single<WeatherRepository> {
        WeatherRepositoryImpl(
            local = get(),
            remote = get(),
        )
    }

    single<LocationRepository> {
        LocationRepositoryImpl(
            applicationContext = get(),
            coroutineContext = Dispatchers.IO,
        )
    }

    viewModelOf(::MainViewModel)
}

val viewModelModule = module {
    viewModel {
        MainViewModel(
        get(),
            get()
        )
    }
}

val networkModule = module {

    // Singleton for Json (Kotlinx Serialization)
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    // Singleton for OkHttpClient
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    // Singleton for Retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get()) // Koin will inject the OkHttpClient defined above
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType()) // Koin injects Json
            )
            .build()
    }

    // Singleton for your ApiService
    single<OpenMeteoArchiveApiService> {
        get<Retrofit>().create(OpenMeteoArchiveApiService::class.java) // Koin injects Retrofit
    }
}
