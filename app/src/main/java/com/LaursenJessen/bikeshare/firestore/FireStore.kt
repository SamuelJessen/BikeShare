package com.LaursenJessen.bikeshare.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


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
                            d.data?.get("Manufacturer").toString(),
                            d.data?.get("Model").toString(),
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