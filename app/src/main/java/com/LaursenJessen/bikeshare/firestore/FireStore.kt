package com.LaursenJessen.bikeshare.firestore

import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
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
                        isLoggedInChanged(true)
                        continuation.resume(signedInUser)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        throw throw Exception("createUserWithEmail: $email failure", task.exception)
                    }
                }
        }
    }
    suspend fun login(email: String, password: String): User {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Something went wrong")
            val signedInUser = user.email?.let { User(user.providerId, it) }
                ?: throw Exception("createUserWithEmail:$email failure")
            isLoggedInChanged(true)
            return signedInUser
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.w(TAG, "Invalid email or password", e)
            throw Exception("Invalid email or password", e)
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.w(TAG, "Invalid user", e)
            throw Exception("Invalid user", e)
        } catch (e: FirebaseTooManyRequestsException) {
            Log.w(TAG, "All requests from this device is blocked due to unusual activity. Try again later", e)
            throw Exception("All requests from this device is blocked due to unusual activity. Try again later", e)
        }
        catch (e: Exception) {
            Log.w(TAG, "login failed", e)
            throw Exception("login failed", e)
        }
    }

}