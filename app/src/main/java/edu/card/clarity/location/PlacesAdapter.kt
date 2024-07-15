package edu.card.clarity.location

import androidx.annotation.RequiresPermission
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesAdapter @Inject constructor(
    private val client: PlacesClient
) {
    @RequiresPermission(
        allOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
        ],
    )
    suspend fun getCurrentPlaces(): FindCurrentPlaceResponse =
        client.findCurrentPlace(currentLocationNameAndTypeRequest).await()

    companion object {
        private val currentLocationNameAndTypeRequest = FindCurrentPlaceRequest.newInstance(
            listOf(Place.Field.NAME, Place.Field.PRIMARY_TYPE)
        )
    }
}