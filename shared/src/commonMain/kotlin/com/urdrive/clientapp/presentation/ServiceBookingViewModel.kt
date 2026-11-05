package com.urdrive.clientapp.presentation

import com.urdrive.clientapp.data.repository.BookingRepository
import com.urdrive.clientapp.domain.model.AddOnPart
import com.urdrive.clientapp.domain.model.Booking
import com.urdrive.clientapp.domain.model.BookingStatus
import com.urdrive.clientapp.domain.model.ServiceItem
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
 * UI State representing the service booking wizard steps and selections.
 */
data class BookingWizardUiState(
    val availableServices: List<ServiceItem> = emptyList(),
    val availableAddOns: List<AddOnPart> = emptyList(),
    val selectedService: ServiceItem? = null,
    val selectedAddOnParts: List<AddOnPart> = emptyList(),
    val scheduledDate: String = "",
    val scheduledTimeSlot: String = "10:00 AM - 12:00 PM",
    val calculatedTotalPrice: Double = 0.0,
    val isLoading: Boolean = false,
    val isBookingSuccessful: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel managing the service booking process, add-on selections,
 * dynamic pricing calculations, and Firestore persistence.
 */
class ServiceBookingViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) {
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow(BookingWizardUiState())
    val uiState: StateFlow<BookingWizardUiState> = _uiState.asStateFlow()

    /**
     * Loads available services and add-on parts from backend repositories.
     */
    fun loadCatalog() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val services = bookingRepository.getAvailableServices()
                val addOns = bookingRepository.getAvailableAddOns()
                _uiState.update {
                    it.copy(
                        availableServices = services,
                        availableAddOns = addOns,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    /**
     * Selects a primary service and recalculates total price.
     */
    fun selectService(service: ServiceItem) {
        _uiState.update { state ->
            val newTotal = service.basePrice + state.selectedAddOnParts.sumOf { it.price }
            state.copy(selectedService = service, calculatedTotalPrice = newTotal)
        }
    }

    /**
     * Toggles selection of an add-on part or check-up and updates total price.
     */
    fun toggleAddOn(addOn: AddOnPart) {
        _uiState.update { state ->
            val updatedList = if (state.selectedAddOnParts.any { it.id == addOn.id }) {
                state.selectedAddOnParts.filter { it.id != addOn.id }
            } else {
                state.selectedAddOnParts + addOn
            }
            val basePrice = state.selectedService?.basePrice ?: 0.0
            val newTotal = basePrice + updatedList.sumOf { it.price }
            state.copy(selectedAddOnParts = updatedList, calculatedTotalPrice = newTotal)
        }
    }

    /**
     * Sets the scheduled date and time slot for the service.
     */
    fun setSchedule(date: String, timeSlot: String) {
        _uiState.update { it.copy(scheduledDate = date, scheduledTimeSlot = timeSlot) }
    }

    /**
     * Submits the completed booking to Firebase Firestore.
     */
    fun submitBooking(userId: String, vehicle: Vehicle) {
        val currentState = _uiState.value
        val service = currentState.selectedService ?: return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val booking = Booking(
                userId = userId,
                vehicleId = vehicle.id,
                vehicleDescription = "${vehicle.year} ${vehicle.make} ${vehicle.model}",
                serviceItem = service,
                selectedAddOns = currentState.selectedAddOnParts,
                scheduledDate = currentState.scheduledDate,
                scheduledTimeSlot = currentState.scheduledTimeSlot,
                totalPrice = currentState.calculatedTotalPrice,
                status = BookingStatus.PENDING,
                createdAtEpochMillis = 0L
            )

            val result = bookingRepository.createBooking(booking)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isBookingSuccessful = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }
}
