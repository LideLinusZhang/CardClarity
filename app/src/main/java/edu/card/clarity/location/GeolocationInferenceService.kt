package edu.card.clarity.location

import androidx.annotation.RequiresPermission
import edu.card.clarity.data.purchase.PlaceTypeToPurchaseTypeMappingDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeolocationInferenceService @Inject constructor(
    private val adapter: PlacesAdapter,
    private val mappingDataSource: PlaceTypeToPurchaseTypeMappingDao
) {
    @RequiresPermission(
        allOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
        ],
    )
    suspend fun getPurchaseTypeInference(): List<GeolocationInference> {
        return adapter.getCurrentPlaces().placeLikelihoods.mapNotNull {
            val name: String = it.place.name ?: return@mapNotNull null
            val placeType: String = it.place.primaryType ?: return@mapNotNull null
            if (name.isEmpty() or placeType.isEmpty()) return@mapNotNull null

            val inferredPurchaseType = mappingDataSource.getPurchaseTypeByPlaceType(placeType)
                ?: return@mapNotNull null

            GeolocationInference(it.likelihood, name, inferredPurchaseType)
        }
    }
}