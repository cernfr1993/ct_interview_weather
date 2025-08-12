package com.example.ceskatelevize_intervie_meteo.repository

import android.content.Context
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.location.LocationManagerCompat
import com.example.ceskatelevize_intervie_meteo.model.Location
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

class LocationRepositoryImpl(
    val applicationContext: Context,
    val coroutineContext: CoroutineContext,
): LocationRepository {
    @Suppress("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        return try {
            withContext(coroutineContext) {
                suspendCoroutineWithTimeout(5_000) { continuation ->
                    runCatching {
                        val locationManager =
                            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        LocationManagerCompat.getCurrentLocation(
                            locationManager,
                            LocationManager.GPS_PROVIDER,
                            null as? CancellationSignal,
                            Executors.newSingleThreadExecutor()
                        ) {
                            it?.let {
                                continuation.resume(Location(it.latitude, it.longitude))
                            } ?: continuation.resume(null)
                        }
                    }.onFailure { continuation.resume(null) }
                }
            }
        } catch (ignored: Exception) {
            null
        }
    }

    private suspend inline fun <T> suspendCoroutineWithTimeout(
        timeoutMilis: Long,
        crossinline block: (Continuation<T>) -> Unit
    ) = withTimeout(timeoutMilis) {
        suspendCancellableCoroutine(block = block)
    }
}
