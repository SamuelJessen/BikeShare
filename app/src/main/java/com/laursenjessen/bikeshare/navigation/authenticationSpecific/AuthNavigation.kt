package com.laursenjessen.bikeshare.navigation.authenticationSpecific

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

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
                    navController.navigate("Login")
                }
                CircularProgressIndicator()
            }
        }
    }
}


