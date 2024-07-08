package edu.card.clarity.location

import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FusedLocationAdapter @Inject constructor(
    private val client: FusedLocationProviderClient
) {
    suspend fun flushLocations() {
        client.flushLocations().await()
    }

    @RequiresPermission(
        allOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
        ],
    )
    suspend fun getCurrentLocation(): Location = client.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).await()
}