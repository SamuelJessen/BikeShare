package com.LaursenJessen.bikeshare.components.rentoutbike.addbikestrava

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AddBikeFromStravaView(nav: NavController) {
    Column() {
        Text("Add bike from Strava")
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Connect to Strava")
        }
    }
}