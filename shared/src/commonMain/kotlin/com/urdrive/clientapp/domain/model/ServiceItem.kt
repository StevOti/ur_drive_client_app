package com.urdrive.clientapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a primary service offering provided by the company
 * (e.g. Routine Oil Change, Brake Pad Replacement, Major Tune-Up).
 */
@Serializable
data class ServiceItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val basePrice: Double = 0.0,
    val estimatedDurationMinutes: Int = 60,
    val category: String = "Maintenance",
    val iconUrl: String = ""
)
