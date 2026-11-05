package com.urdrive.clientapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a service booking created by a client.
 * Combines the vehicle, primary service, selected add-on parts/check-ups,
 * scheduling details, pricing calculation, and current status.
 */
@Serializable
data class Booking(
    val id: String = "",
    val userId: String = "",
    val vehicleId: String = "",
    val vehicleDescription: String = "", // e.g. "2023 Toyota Camry"
    val serviceItem: ServiceItem? = null,
    val selectedAddOns: List<AddOnPart> = emptyList(),
    val scheduledDate: String = "",      // e.g. "2023-11-15"
    val scheduledTimeSlot: String = "",  // e.g. "10:00 AM - 12:00 PM"
    val totalPrice: Double = 0.0,
    val status: BookingStatus = BookingStatus.PENDING,
    val createdAtEpochMillis: Long = 0L
)

/**
 * Tracks the lifecycle status of a service booking.
 */
@Serializable
enum class BookingStatus {
    PENDING,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
