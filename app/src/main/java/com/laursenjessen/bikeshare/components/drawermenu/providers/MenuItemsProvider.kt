package com.laursenjessen.bikeshare.components.drawermenu.providers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.navigation.NavController
import com.laursenjessen.bikeshare.components.drawermenu.models.DrawerMenuItemModel
import com.google.firebase.auth.FirebaseAuth

fun getMenuItems(navController: NavController, auth: FirebaseAuth): List<DrawerMenuItemModel> {
    return listOf(
        DrawerMenuItemModel("1", "Home", Icons.Rounded.Home, "HomeScreenMenu") {
            navController.navigate("HomeScreen")
        },
        DrawerMenuItemModel("2", "Rent bikes", Icons.Filled.DirectionsBike, "RentBikes") {
            navController.navigate("RentBikeView")
        },
        DrawerMenuItemModel("3", "My bikes", Icons.Rounded.PlaylistAdd, "MyBikes") {
            navController.navigate("MyBikesView")
        },
        DrawerMenuItemModel("4", "My rentals", Icons.Filled.List, "MyRentals") {
            navController.navigate("MyRentalsView")
        },
        DrawerMenuItemModel("5", "Logout", Icons.Rounded.ExitToApp, "Logout") {
            auth.signOut()
            navController.navigate("Login")
        },
    )
}

