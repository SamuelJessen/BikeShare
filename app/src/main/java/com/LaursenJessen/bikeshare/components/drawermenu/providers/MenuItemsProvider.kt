package com.LaursenJessen.bikeshare.components.drawermenu.providers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Home
import com.LaursenJessen.bikeshare.components.drawermenu.models.DrawerMenuItemModel

import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

fun getMenuItems(navController: NavController, auth: FirebaseAuth): List<DrawerMenuItemModel> {
    return listOf(
        DrawerMenuItemModel("1", "Home", Icons.Rounded.Home, "HomeScreenMenu") {
            navController.navigate("HomeScreen")
        },
        DrawerMenuItemModel("2", "Add bike", Icons.Rounded.Add, "AddBikeMenu") {
            navController.navigate("AddBike")
        },
        DrawerMenuItemModel("3", "Logout", Icons.Rounded.ArrowBack, "Logout") {
            auth.signOut()
            navController.navigate("Login")
        },
    )
}
