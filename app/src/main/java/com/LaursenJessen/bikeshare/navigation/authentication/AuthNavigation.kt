package com.LaursenJessen.bikeshare.components.authentication.authnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun NavGraphBuilder.authenticatedComposable(
    route: String,
    navController: NavController,
    isAuthenticated: Boolean,
    content: @Composable () -> Unit
) {
    composable(route) {
        if (isAuthenticated) {
            content()
        } else {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != "Login") {
                navController.navigate("Login")
            }
        }
    }
}
