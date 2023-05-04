package com.LaursenJessen.bikeshare


import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.LaursenJessen.bikeshare.components.home.PositionService
import com.LaursenJessen.bikeshare.navigation.authentication.AuthenticationViewModel
import com.LaursenJessen.bikeshare.components.navigation.AppNavigation
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.authenticationStateViewModel.AuthenticationViewModel
import com.LaursenJessen.bikeshare.ui.theme.BikeShareTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.LaursenJessen.bikeshare.components.navigation.AppNavigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationService: PositionService
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationService = PositionService(fusedLocationClient, this)
        val auth = Firebase.auth
        val api = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        FirebaseApp.initializeApp(this)
        val service = FireStore(storage, api, auth) { auth.currentUser != null }
        setContent {
            BikeShareTheme(darkTheme = false) {
                AppNavigation(authViewModel, service, auth, locationService)
            }
        }
    }
}
