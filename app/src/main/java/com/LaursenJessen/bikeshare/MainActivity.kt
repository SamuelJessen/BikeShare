package com.LaursenJessen.bikeshare


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
<<<<<<< HEAD
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.LaursenJessen.bikeshare.navigation.authentication.AuthenticationViewModel
=======
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.LaursenJessen.bikeshare.components.authentication.Login
import com.LaursenJessen.bikeshare.components.authentication.Signup
import com.LaursenJessen.bikeshare.components.home.HomeScreen
import com.LaursenJessen.bikeshare.components.rentbike.RentBikeMain
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikes.AddBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikestrava.AddBikeFromStravaView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.MyBikesView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.mybike.EditBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.mybike.MyBikeView
>>>>>>> origin/master
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.ui.theme.BikeShareTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.LaursenJessen.bikeshare.components.navigation.AppNavigation
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthenticationViewModel>()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        val api = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        FirebaseApp.initializeApp(this)
        val service = FireStore(storage, api, auth) { auth.currentUser != null }
        setContent {
            BikeShareTheme() {
                AppNavigation(authViewModel, service, auth)
            }
        }
    }
}
