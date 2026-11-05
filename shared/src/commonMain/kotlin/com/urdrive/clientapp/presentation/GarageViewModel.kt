package com.urdrive.clientapp.presentation

import com.urdrive.clientapp.data.repository.VehicleRepository
import com.urdrive.clientapp.domain.model.Vehicle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State representing the client's garage and vehicle management.
 */
data class GarageUiState(
    val vehicles: List<Vehicle> = emptyList(),
    val selectedVehicle: Vehicle? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel managing vehicle garage operations using MVVM + UDF.
 */
class GarageViewModel(
    private val vehicleRepository: VehicleRepository = VehicleRepository()
) {
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow(GarageUiState())
    val uiState: StateFlow<GarageUiState> = _uiState.asStateFlow()

    /**
     * Loads vehicles for the given user ID.
     */
    fun loadUserVehicles(userId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                vehicleRepository.getUserVehicles(userId).collect { list ->
                    _uiState.update { state ->
                        state.copy(
                            vehicles = list,
                            selectedVehicle = state.selectedVehicle ?: list.firstOrNull(),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    /**
     * Selects a specific vehicle in the garage.
     */
    fun selectVehicle(vehicle: Vehicle) {
        _uiState.update { it.copy(selectedVehicle = vehicle) }
    }

    /**
     * Adds a new vehicle to the garage.
     */
    fun addVehicle(vehicle: Vehicle, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = vehicleRepository.addVehicle(vehicle)
            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }
}
