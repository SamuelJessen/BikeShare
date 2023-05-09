package com.laursenjessen.bikeshare.components.rentBikes.bikeRental

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.laursenjessen.bikeshare.components.helpers.bikeImageWithFallback.BikeImageWithIconFallback
import com.laursenjessen.bikeshare.services.firestore.models.Bike

@Composable
fun BikeRentListItem(bike: Bike, nav: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = bike.name, style = MaterialTheme.typography.h5)
            Text(
                text = "Preliminary ride distance: ${bike.distance}km",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Price per day: ${bike.dailyPrice} DKK",
                style = MaterialTheme.typography.body2
            )
            if (!bike.rentedOut) {
                Text(
                    text = "Status: Available",
                    style = MaterialTheme.typography.body2,
                    color = Color.Green
                )
            } else {
                Text(
                    text = "Status: Unknown",
                    style = MaterialTheme.typography.body2,
                    color = Color.Red
                )
            }
            BikeImageWithIconFallback(
                imageUrl = bike.imageUrl,
                contentDescription = "Bike image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(vertical = 0.dp)
                    .clip(MaterialTheme.shapes.medium),
                iconSize = 120.dp
            )
            Button(onClick = { nav.navigate("BikeRental/${bike.id}") }) {
                Text(text = "Show")
            }
        }
    }
}