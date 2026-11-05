package com.urdrive.clientapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a complementary service part or check-up that a client
 * can select to enhance their service booking.
 */
@Serializable
data class AddOnPart(
    val id: String = "",
    val name: String = "",         // e.g. "Cabin Air Filter Replacement" or "Coolant Flush"
    val description: String = "",
    val price: Double = 0.0,
    val type: AddOnType = AddOnType.PART,
    val isSelected: Boolean = false
)

/**
 * Categorizes add-on items into parts or specialized check-ups.
 */
@Serializable
enum class AddOnType {
    PART,
    CHECKUP
}
