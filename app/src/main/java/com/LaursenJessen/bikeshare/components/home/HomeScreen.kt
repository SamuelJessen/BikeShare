package com.LaursenJessen.bikeshare.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(nav: NavController) {
    Column() {
        Text("HomeScreen")
        Button(onClick = { nav.navigate("RentBikeView") }) {
            Text(text = "Rent Bike")
        }
        Button(onClick = { nav.navigate("RentOutBikeView") }) {
            Text(text = "Rent Out Bike")
        }
    }
}