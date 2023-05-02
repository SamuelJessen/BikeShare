package com.LaursenJessen.bikeshare.components.rentbike

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.firestore.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore


@Composable
fun RentBikeView(service: FireStore, nav: NavController) {
    val bikeList = remember { mutableStateOf(emptyList<Bike>()) }
    LaunchedEffect(Unit) {
        bikeList.value = service.getBikes()
    }
    val availableBikes = bikeList.value.filter { bike ->
        !bike.rentedOut && bike.userId != service.auth.uid
    }
    if (bikeList.value.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (availableBikes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No available bikes",
                style = MaterialTheme.typography.subtitle1
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(availableBikes) { bike ->
                BikeListItem(bike = bike, nav = nav)
            }
        }
    }
}
