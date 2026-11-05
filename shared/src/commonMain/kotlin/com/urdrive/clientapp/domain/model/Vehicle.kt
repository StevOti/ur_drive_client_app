package com.urdrive.clientapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a client's vehicle registered in the platform garage.
 * Used for booking services, tracking service history, and vehicle diagnostics.
 */
@Serializable
data class Vehicle(
    val id: String = "",
    val userId: String = "",
    val make: String = "",         // e.g. Toyota, BMW, Ford
    val model: String = "",        // e.g. Camry, X5, F-150
    val year: Int = 2023,          // Manufacture year
    val licensePlate: String = "", // Vehicle registration plate
    val mileage: Int = 0,          // Current odometer reading in miles or kilometers
    val imageUrl: String = ""      // Optional photo URL of the vehicle
)
