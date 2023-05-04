package com.LaursenJessen.bikeshare.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(nav: NavController, positionService: PositionService) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
            Button(onClick = { nav.navigate("RentBikeView") }) {
                Text(text = "Rent Bike")
            }
            Button(onClick = { nav.navigate("MyBikesView") }) {
                Text(text = "Rent Out Bike/My bikes")
            }
        }
        WeatherView(locationService = positionService, Modifier.weight(1f))
    }
}