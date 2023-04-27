package com.LaursenJessen.bikeshare.components.rentoutbike.mybikes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun MyBikesMain(service: FireStore, nav: NavController) {
    val bikes = remember { mutableStateOf(emptyList<Bike>()) }
    LaunchedEffect(Unit){
        val list = service.getBikes()
        bikes.value = list
    }
    Column {
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
            .clickable(onClick = {nav.navigate("MyBikeItem")}),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(bike.imageUrl)
            Image(
                painter = rememberImagePainter(storageRef),
                contentDescription = "Bike image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = bike.manufacturer + bike.model,
                textAlign = TextAlign.Center,
            )
        }
    }
}