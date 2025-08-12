package com.example.ceskatelevize_intervie_meteo

import android.app.Application
import com.example.ceskatelevize_intervie_meteo.di.mainModule
import com.example.ceskatelevize_intervie_meteo.di.networkModule
import com.example.ceskatelevize_intervie_meteo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(networkModule, mainModule, viewModelModule)
        }
    }
}