package com.LaursenJessen.bikeshare.firestore

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStore(
    private val storage: FirebaseStorage,
    private val api: FirebaseFirestore,
    val auth: FirebaseAuth,
    private val isLoggedInChanged: (Boolean) -> Unit
) {
    companion object {
        const val TAG = "FIRE_STORE_SERVICE"
    }

    suspend fun getBikes(): List<Bike> {
        return suspendCoroutine { continuation ->
            api.collection("Bikes").get().addOnSuccessListener {
                    val bikes = it.documents.map { d ->
                        Bike(
                            d.id,
                            d.data?.get("Address").toString(),
                            d.data?.get("Description").toString(),
                            d.data?.get("Name").toString(),
                            d.data?.get("DailyPrice")?.toString()?.toDoubleOrNull()?.toInt() ?: 0,
                            d.data?.get("Distance")?.toString()?.toDoubleOrNull()?.toInt() ?: 0,
                            d.data?.get("RentedOut").toString().toBoolean(),
                            d.data?.get("UserId").toString(),
                            d.data?.get("ImageUrl").toString()
                        )
                    }
                    continuation.resume(bikes)
                }.addOnFailureListener { e ->
                    Log.v(TAG, "ERROR: Could not get bikes", e)
                    continuation.resumeWithException(e)
                }
        }
    }

    suspend fun getBikeById(bikeId: String): Bike? {
        return try {
            val documentSnapshot = api.collection("Bikes").document(bikeId).get().await()
            if (documentSnapshot.exists()) {
                val data = documentSnapshot.data ?: mapOf()
                Bike(
                    id = documentSnapshot.id,
                    address = data["Address"].toString(),
                    description = data["Description"].toString(),
                    name = data["Name"].toString(),
                    dailyPrice = data["DailyPrice"]?.toString()?.toDoubleOrNull()?.toInt() ?: 0,
                    distance = data["Distance"]?.toString()?.toDoubleOrNull()?.toInt() ?: 0,
                    rentedOut = data["RentedOut"].toString().toBoolean(),
                    userId = data["UserId"].toString(),
                    imageUrl = data["ImageUrl"].toString()
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting bike by ID: $e")
            null
        }
    }

    suspend fun addRentalDocument(rental: Rental) = suspendCoroutine { continuation ->
        api.collection("Rentals").document(rental.id).set(
                mapOf(
                    "Bike" to rental.bike,
                    "UserId" to rental.userId,
                    "UserEmail" to rental.userEmail,
                    "BikeId" to rental.bikeId,
                    "RentDurationDays" to rental.rentDurationDays,
                    "DailyPrice" to rental.dailyPrice,
                    "RentedAt" to Timestamp.now()
                )
            ).addOnSuccessListener {
                Log.d(TAG, "Rental document added successfully")
                continuation.resume(Unit)
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding rental document", e)
                continuation.resumeWithException(e)
            }
    }

    suspend fun addBike(bike: Bike) = suspendCoroutine { continuation ->
        api.collection("Bikes").document(bike.id).set(
                mapOf(
                    "Address" to bike.address,
                    "Description" to bike.description,
                    "Name" to bike.name,
                    "DailyPrice" to bike.dailyPrice,
                    "Distance" to bike.distance,
                    "RentedOut" to bike.rentedOut,
                    "UserId" to bike.userId,
                    "ImageUrl" to bike.imageUrl
                )
            ).addOnSuccessListener {
                Log.d(TAG, "Bike added successfully")
                continuation.resume(Unit)
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding bike", e)
                continuation.resumeWithException(e)
            }
    }

    suspend fun deleteBike(bikeId: String) = suspendCoroutine { continuation ->
        api.collection("Bikes").document(bikeId).delete().addOnSuccessListener {
                Log.d(TAG, "Bike deleted successfully")
                continuation.resume(Unit)
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error deleting bike", e)
                continuation.resumeWithException(e)
            }
    }

    suspend fun updateBikeRentedStatus(bikeId: String, rentedOut: Boolean) =
        suspendCoroutine { continuation ->
            api.collection("Bikes").document(bikeId).update("RentedOut", rentedOut)
                .addOnSuccessListener {
                    Log.d(TAG, "Bike rented status updated successfully")
                    continuation.resume(Unit)
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error updating bike rented status", e)
                    continuation.resumeWithException(e)
                }
        }

    suspend fun signup(email: String, password: String) {
        suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser ?: throw Exception("Something wrong")
                        val signedInUser = user.email?.let { User(user.providerId, it) }
                            ?: throw Exception("createUserWithEmail:$email failure")
                        continuation.resume(signedInUser)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        throw throw Exception("createUserWithEmail: $email failure", task.exception)
                    }
                }
        }
    }

    suspend fun login(email: String, password: String): User {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser ?: throw Exception("Something wrong")
                        val signedInUser = user.email?.let { User(user.providerId, it) }
                            ?: throw Exception("createUserWithEmail:$email failure")
                        continuation.resume(signedInUser)
                    } else {
                        Log.w(TAG, "loginUserWithEmail:failure", task.exception)
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }
    }
}