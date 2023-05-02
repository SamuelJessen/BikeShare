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


class FireStore(private val storage: FirebaseStorage, private val api: FirebaseFirestore, val auth: FirebaseAuth, private val isLoggedInChanged: (Boolean) -> Unit) {
    companion object {
        const val TAG = "FIRE_STORE_SERVICE"
    }
    suspend fun getBikes(): List<Bike> {
        return suspendCoroutine { continuation ->
            api.collection("Bikes")
                .get()
                .addOnSuccessListener {
                    val bikes =
                        it.documents.map { d -> Bike(
                            d.id,
                            d.data?.get("Address").toString(),
                            d.data?.get("Description").toString(),
                            d.data?.get("Name").toString(),
                            d.data?.get("Distance").toString().toFloat(),
                            d.data?.get("RentedOut").toString().toBoolean(),
                            d.data?.get("UserId").toString(),
                            d.data?.get("ImageUrl").toString())
                            }
                    continuation.resume(bikes)
                }.addOnFailureListener {
                    Log.v(TAG, "We failed $it")
                    throw it
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
                    distance = data["Distance"].toString().toFloat(),
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
        api.collection("Rentals").document(rental.id)
            .set(mapOf(
                "bike" to rental.bike,
                "userId" to rental.userId,
                "userEmail" to rental.userEmail,
                "bikeId" to rental.bikeId,
                "rentDuration" to rental.rentDuration,
                "phoneNumber" to rental.phoneNumber,
                "rentedAt" to Timestamp.now()
            ))
            .addOnSuccessListener {
                Log.d(TAG, "Rental document added successfully")
                continuation.resume(Unit)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding rental document", e)
                continuation.resumeWithException(e)
            }
    }

    suspend fun addBike(bike: Bike) = suspendCoroutine { continuation ->
        api.collection("Bikes").document(bike.id)
            .set(mapOf(
                "Address" to bike.address,
                "Description" to bike.description,
                "Name" to bike.name,
                "Distance" to bike.distance,
                "RentedOut" to bike.rentedOut,
                "UserId" to bike.userId,
                "ImageUrl" to bike.imageUrl
            ))
            .addOnSuccessListener {
                Log.d(TAG, "Bike added successfully")
                continuation.resume(Unit)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding bike", e)
                continuation.resumeWithException(e)
            }
    }

    suspend fun signup(email: String, password: String) {
        suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
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

    suspend fun login(email: String, password: String) {
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser ?: throw Exception("Something wrong")
                        val signedInUser = user.email?.let { User(user.providerId, it) }
                            ?: throw Exception("createUserWithEmail:$email failure")
                        continuation.resume(signedInUser)
                    } else {
                        Log.w(TAG, "loginUserWithEmail:failure", task.exception)
                        throw throw Exception("loginUserWithEmail: $email failure", task.exception)
                    }
                }
        }
    }
}