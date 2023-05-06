package com.laursenjessen.bikeshare.components.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.laursenjessen.bikeshare.components.authentication.Login
import com.laursenjessen.bikeshare.components.authentication.Signup
import com.laursenjessen.bikeshare.components.authentication.authnav.authenticatedComposable
import com.laursenjessen.bikeshare.components.drawermenu.ScaffoldWithMenuContent
import com.laursenjessen.bikeshare.components.drawermenu.providers.getMenuItems
import com.laursenjessen.bikeshare.components.home.HomeScreen
import com.laursenjessen.bikeshare.components.rentBikes.BikeRentalDetails
import com.laursenjessen.bikeshare.components.rentBikes.RentBikeView
import com.laursenjessen.bikeshare.components.rentOutBike.addBikes.AddBikeView
import com.laursenjessen.bikeshare.components.rentOutBike.myBikes.MyBikesView
import com.laursenjessen.bikeshare.components.rentOutBike.myBikes.myBike.EditBikeView
import com.laursenjessen.bikeshare.components.rentOutBike.myBikes.myBike.MyBikeView
import com.laursenjessen.bikeshare.components.rentOutBike.myRentals.MyRentalsView
import com.laursenjessen.bikeshare.navigation.authenticationStateViewModel.AuthenticationViewModel
import com.laursenjessen.bikeshare.services.firestore.FireStore
import com.laursenjessen.bikeshare.services.position.PositionService
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavigation(
    authViewModel: AuthenticationViewModel,
    service: FireStore,
    auth: FirebaseAuth,
    locationService: PositionService
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val menuItems = getMenuItems(navController = navController, auth)

    Surface(color = MaterialTheme.colors.background) {
        NavHost(
            navController,
            startDestination = if (authViewModel.isAuthenticated) "HomeScreen" else "Login"
        ) {
            composable("Login") {
                Login(
                    service, nav = navController, authViewModel = authViewModel
                )
            }
            composable("Signup") {
                Signup(
                    service, nav = navController, authViewModel = authViewModel
                )
            }
            authenticatedComposable("HomeScreen", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(
                    scaffoldState, scope, menuItems, topBarText = "Home"
                ) { HomeScreen(nav = navController, locationService) }
            }
            authenticatedComposable("RentBikeView", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(
                    scaffoldState, scope, menuItems, topBarText = "Rent bike"
                ) { RentBikeView(nav = navController, service = service) }
            }
            authenticatedComposable("AddBike", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(
                    scaffoldState,
                    scope,
                    menuItems,
                    topBarText = "Add bike",
                    nav = navController,
                    showBackButton = true
                ) { AddBikeView(nav = navController, service = service) }
            }

            authenticatedComposable("MyBikesView", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(
                    scaffoldState, scope, menuItems, topBarText = "My bikes"
                ) { MyBikesView(nav = navController, service = service) }
            }
            authenticatedComposable("MyRentalsView", navController, authViewModel.isAuthenticated) {
                ScaffoldWithMenuContent(
                    scaffoldState, scope, menuItems, topBarText = "My rentals"
                ) { MyRentalsView(nav = navController, service = service) }
            }
            authenticatedComposable(
                "MyBikeView/{bikeId}", navController, authViewModel.isAuthenticated
            ) {
                ScaffoldWithMenuContent(
                    scaffoldState,
                    scope,
                    menuItems,
                    topBarText = "My bike",
                    nav = navController,
                    showBackButton = true
                ) { MyBikeView(nav = navController, service = service) }
            }
            authenticatedComposable(
                "EditBikeView/{bikeId}", navController, authViewModel.isAuthenticated
            ) {
                ScaffoldWithMenuContent(
                    scaffoldState,
                    scope,
                    menuItems,
                    topBarText = "Edit bike",
                    nav = navController,
                    showBackButton = true
                ) { EditBikeView(nav = navController, service = service) }
            }
            authenticatedComposable(
                "BikeRental/{bikeId}", navController, authViewModel.isAuthenticated
            ) {
                ScaffoldWithMenuContent(
                    scaffoldState,
                    scope,
                    menuItems,
                    topBarText = "Bike Rental",
                    showBackButton = true,
                    nav = navController
                ) { BikeRentalDetails(nav = navController, service = service) }
            }
        }
    }
}