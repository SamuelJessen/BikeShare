package com.LaursenJessen.bikeshare.services.position

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PositionService(
    private val client: FusedLocationProviderClient,
    private val activity: Activity,
) {
    val locationOn = mutableStateOf(false)

    companion object {
        const val REQUEST_ID = 9
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun getCurrentLocation(): Location {
        return suspendCoroutine { continuation ->
            try {
                client.getCurrentLocation(
                    LocationRequest.QUALITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener {
                    continuation.resume(it)
                }.addOnFailureListener {
                    Log.v(this.javaClass.name, "Location request failure")
                    requestPermission()
                }
            } catch (e: SecurityException) {
                Log.v(this.javaClass.name, "Location request not allowed")
            }
        }
    }
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (!checkPermission()) {
            locationOn.value = false
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ID
            )
        } else {
            locationOn.value = true
        }
    }
}
