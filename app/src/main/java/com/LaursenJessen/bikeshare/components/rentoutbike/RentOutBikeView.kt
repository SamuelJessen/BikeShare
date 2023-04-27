package com.LaursenJessen.bikeshare.components.rentoutbike

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RentOutBikeMain(nav: NavController) {
    Column() {
        Text("Rent Out Bikes")
        Button(onClick = { nav.navigate("MyBikesView") }) {
            Text(text = "My bikes")
        }
        Button(onClick = { nav.navigate("AddBike") }) {
            Text(text = "Add bike")
        }
        Button(onClick = { nav.navigate("AddBikeStrava") }) {
            Text(text = "Add bike from Strava")
        }
    }
}