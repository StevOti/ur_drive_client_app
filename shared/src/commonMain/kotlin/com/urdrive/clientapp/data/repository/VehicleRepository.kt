package com.urdrive.clientapp.data.repository

import com.urdrive.clientapp.domain.model.Vehicle
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository responsible for managing vehicle data in Firebase Firestore
 * for the authenticated user's garage.
 */
class VehicleRepository {
    private val firestore = Firebase.firestore
    private val vehiclesCollection = firestore.collection("vehicles")

    /**
     * Retrieves real-time stream or list of vehicles belonging to a specific user.
     */
    fun getUserVehicles(userId: String): Flow<List<Vehicle>> = flow {
        try {
            val snapshot = vehiclesCollection.get()
            val vehicles = snapshot.documents.mapNotNull { doc ->
                try {
                    val vehicle = doc.data<Vehicle>()
                    if (vehicle.userId == userId) vehicle.copy(id = doc.id) else null
                } catch (e: Exception) {
                    null
                }
            }
            emit(vehicles)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    /**
     * Adds a new vehicle to the user's garage in Firestore.
     */
    suspend fun addVehicle(vehicle: Vehicle): Result<String> {
        return try {
            val docRef = vehiclesCollection.add(vehicle)
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes a vehicle from the garage by ID.
     */
    suspend fun deleteVehicle(vehicleId: String): Result<Unit> {
        return try {
            vehiclesCollection.document(vehicleId).delete()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
