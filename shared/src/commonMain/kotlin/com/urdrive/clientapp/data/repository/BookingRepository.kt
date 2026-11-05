package com.urdrive.clientapp.data.repository

import com.urdrive.clientapp.domain.model.AddOnPart
import com.urdrive.clientapp.domain.model.Booking
import com.urdrive.clientapp.domain.model.ServiceItem
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository responsible for handling service catalog, add-ons,
 * and service booking persistence in Firebase Firestore.
 */
class BookingRepository {
    private val firestore = Firebase.firestore
    private val bookingsCollection = firestore.collection("bookings")
    private val servicesCollection = firestore.collection("services")
    private val addOnsCollection = firestore.collection("addons")

    /**
     * Fetches available primary service offerings from Firestore.
     */
    suspend fun getAvailableServices(): List<ServiceItem> {
        return try {
            val snapshot = servicesCollection.get()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data<ServiceItem>().copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Fetches available complementary add-on parts and check-ups from Firestore.
     */
    suspend fun getAvailableAddOns(): List<AddOnPart> {
        return try {
            val snapshot = addOnsCollection.get()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data<AddOnPart>().copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Creates a new service booking in Firestore.
     */
    suspend fun createBooking(booking: Booking): Result<String> {
        return try {
            val docRef = bookingsCollection.add(booking)
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves real-time stream or list of user bookings.
     */
    fun getUserBookings(userId: String): Flow<List<Booking>> = flow {
        try {
            val snapshot = bookingsCollection.get()
            val bookings = snapshot.documents.mapNotNull { doc ->
                try {
                    val booking = doc.data<Booking>()
                    if (booking.userId == userId) booking.copy(id = doc.id) else null
                } catch (e: Exception) {
                    null
                }
            }
            emit(bookings)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}
