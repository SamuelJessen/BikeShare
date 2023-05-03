package com.LaursenJessen.bikeshare.components.drawermenu.providers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ViewList
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.components.drawermenu.models.DrawerMenuItemModel
import com.google.firebase.auth.FirebaseAuth

fun getMenuItems(navController: NavController, auth: FirebaseAuth): List<DrawerMenuItemModel> {
    return listOf(
        DrawerMenuItemModel("1", "Home", Icons.Rounded.Home, "HomeScreenMenu") {
            navController.navigate("HomeScreen")
        },
        DrawerMenuItemModel("2", "Rent bikes", Icons.Filled.DirectionsBike, "RentBikes") {
            navController.navigate("RentBikeView")
        },
        DrawerMenuItemModel("3", "Add bike", Icons.Default.Add, "AddBikeMenu") {
            navController.navigate("AddBike")
        },
        DrawerMenuItemModel("4", "My bikes", Icons.Rounded.ViewList, "MyBikes") {
            navController.navigate("MyBikesView")
        },
        DrawerMenuItemModel("5", "Logout", Icons.Rounded.ExitToApp, "Logout") {
            auth.signOut()
            navController.navigate("Login")
        },
    )
}

