package com.LaursenJessen.bikeshare.components.authentication.authnav

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

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
                LaunchedEffect(Unit) {
                    delay(1000) // Delay for 1 second
                    navController.navigate("Login")
                }
                CircularProgressIndicator()
            }
        }
    }
}


