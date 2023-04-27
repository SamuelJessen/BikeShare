package com.LaursenJessen.bikeshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.LaursenJessen.bikeshare.components.authentication.Login
import com.LaursenJessen.bikeshare.components.authentication.Signup
import com.LaursenJessen.bikeshare.components.home.HomeScreen
import com.LaursenJessen.bikeshare.components.rentbike.RentBikeMain
import com.LaursenJessen.bikeshare.components.rentoutbike.RentOutBikeMain
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikes.AddBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikestrava.AddBikeFromStravaView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.MyBikesMain
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.ui.theme.BikeShareTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        FirebaseApp.initializeApp(this);
        val service = FireStore(auth)
        auth.currentUser
        setContent {
            BikeShareTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "Login") {
                        composable("Signup") { Signup(service, nav = navController) }
                        composable("Login") { Login(service, nav = navController) }
                        composable("HomeScreen") { HomeScreen(nav = navController) }
                        composable("RentBikeView") { RentBikeMain(nav = navController) }
                        composable("RentOutBikeView") { RentOutBikeMain(nav = navController) }
                        composable("MyBikesView") { MyBikesMain(nav = navController) }
                        composable("AddBike") { AddBikeView(nav = navController) }
                        composable("AddBikeStrava") { AddBikeFromStravaView(nav = navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BikeShareTheme {
        Greeting("Android")
    }
}