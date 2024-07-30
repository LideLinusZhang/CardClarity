package edu.card.clarity.presentation.purchaseBenefitsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.location.GeolocationInference
import edu.card.clarity.location.GeolocationInferenceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeolocationViewModel @Inject constructor(
    private val geolocationInferenceService: GeolocationInferenceService
) : ViewModel() {
    private val _geolocationInference = MutableStateFlow<List<GeolocationInference>?>(null)
    val geolocationInference: StateFlow<List<GeolocationInference>?> = _geolocationInference

    fun fetchGeolocationInference() {
        viewModelScope.launch {
            try {
                val inferences = geolocationInferenceService.getPurchaseTypeInference()
                Log.d("Inferences", inferences.toString())
                _geolocationInference.value = inferences
            } catch (e: SecurityException) {
                // Handle the case where permissions were not granted
            }
        }
    }
}
