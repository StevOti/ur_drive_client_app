package com.urdrive.clientapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents an authenticated user profile stored in Firestore and managed via Firebase Auth.
 */
@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
    val createdAtEpochMillis: Long = 0L,
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val gender: String = "",
    val idNumber: String = "",
    val address: String = ""
)
