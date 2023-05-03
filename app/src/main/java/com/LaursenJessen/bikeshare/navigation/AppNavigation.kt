package com.LaursenJessen.bikeshare.components.navigation

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.LaursenJessen.bikeshare.components.authentication.Login
import com.LaursenJessen.bikeshare.components.authentication.Signup
import com.LaursenJessen.bikeshare.components.authentication.authnav.authenticatedComposable
import com.LaursenJessen.bikeshare.components.drawermenu.ScaffoldWithMenuContent
import com.LaursenJessen.bikeshare.components.drawermenu.providers.getMenuItems
import com.LaursenJessen.bikeshare.components.home.HomeScreen
import com.LaursenJessen.bikeshare.components.rentbike.BikeRentalDetails
import com.LaursenJessen.bikeshare.components.rentbike.RentBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.RentOutBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikes.AddBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikestrava.AddBikeFromStravaView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.MyBikesView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.mybike.MyBikeView
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.navigation.authentication.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavigation(authViewModel: AuthenticationViewModel, service: FireStore, auth: FirebaseAuth) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val menuItems = getMenuItems(navController = navController, auth)

    Surface(color = MaterialTheme.colors.background) {
        NavHost(navController, startDestination = if (authViewModel.isAuthenticated) "HomeScreen" else "Login") {
            composable("Login") { Login(service, nav = navController, authViewModel = authViewModel) }
            composable("Signup") { Signup(service, nav = navController, authViewModel = authViewModel) }
            authenticatedComposable("HomeScreen", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText = "Home") { HomeScreen(nav = navController) }
            }
            authenticatedComposable("RentBikeView", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText = "Rent bike") { RentBikeView(nav = navController, service = service) }
            }
            authenticatedComposable("AddBike", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText = "Add bike") { AddBikeView(nav = navController, service = service) }
            }
            authenticatedComposable("RentOutBikeView", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText = "Rent out bike") { RentOutBikeView(nav = navController) }
            }
            authenticatedComposable("MyBikesView", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText = "My bikes") { MyBikesView(nav = navController, service = service) }
            }
            authenticatedComposable("AddBikeStrava", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText = "Add bike from Strava") { AddBikeFromStravaView(nav = navController) }
            }
            authenticatedComposable("MyBikeView/{bikeId}", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems) { MyBikeView(nav = navController, service = service) }
            }
            authenticatedComposable("BikeRental/{bikeId}", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(scaffoldState, scope, menuItems, topBarText= "Bike Rental", showBackButton = true, nav = navController) { BikeRentalDetails(nav = navController, service = service) }
            }

        }
    }
}