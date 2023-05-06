package com.laursenjessen.bikeshare


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.laursenjessen.bikeshare.components.navigation.AppNavigation
import com.laursenjessen.bikeshare.navigation.authenticationStateViewModel.AuthenticationViewModel
import com.laursenjessen.bikeshare.services.firestore.FireStore
import com.laursenjessen.bikeshare.services.position.PositionService
import com.laursenjessen.bikeshare.ui.theme.BikeShareTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationService: PositionService
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationService = PositionService(fusedLocationClient, this)
        val auth = Firebase.auth
        val api = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        FirebaseApp.initializeApp(this)
        val service = FireStore(storage, api, auth)
        setContent {
            BikeShareTheme(darkTheme = false) {
                AppNavigation(authViewModel = authViewModel, service = service, auth = auth, locationService = locationService)
            }
        }
    }
}
