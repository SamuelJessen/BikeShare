package com.LaursenJessen.bikeshare


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.LaursenJessen.bikeshare.navigation.authentication.AuthenticationViewModel
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.ui.theme.BikeShareTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.LaursenJessen.bikeshare.components.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthenticationViewModel>()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        FirebaseApp.initializeApp(this)
        val service = FireStore(auth) { auth.currentUser != null }
        setContent {
            BikeShareTheme() {
                AppNavigation(authViewModel, service, auth)
            }
        }
    }
}






