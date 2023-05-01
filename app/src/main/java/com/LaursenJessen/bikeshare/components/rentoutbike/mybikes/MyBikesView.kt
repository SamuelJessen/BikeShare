package com.LaursenJessen.bikeshare.components.rentoutbike.mybikes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore

@Composable
fun MyBikesView(service: FireStore, nav: NavController) {
    val bikes = remember { mutableStateOf(emptyList<Bike>()) }
    LaunchedEffect(Unit){
        val list = service.getBikes()
        bikes.value = list
    }
    var expanded by remember { mutableStateOf(false) }
    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "My Bikes",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = {
                    nav.navigate("AddBike")
                    expanded = false
                }) {
                    Text(text = "Add bike manually")
                }
                DropdownMenuItem(onClick = {
                    nav.navigate("AddBikeStrava")
                    expanded = false
                }) {
                    Text(text = "Add bike with Strava")
                }
            }
        }
        val bikesForUser = bikes.value.filter{it.userId == service.auth.uid}
        bikesForUser.map {
            BikeListItem(it, nav)
    }
    }
}

@Composable
fun BikeListItem(bike: Bike,nav: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = { nav.navigate("MyBikeView") }),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val storageRef = bike.imageUrl
            Image(
                painter = rememberImagePainter(storageRef),
                contentDescription = "Bike image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = bike.name,
                textAlign = TextAlign.Center,
            )
        }
    }
}