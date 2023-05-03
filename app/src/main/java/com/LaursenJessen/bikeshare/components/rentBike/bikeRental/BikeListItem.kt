package com.LaursenJessen.bikeshare.components.rentBike

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.models.Bike

@Composable
fun BikeListItem(bike: Bike, nav: NavController) {
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
            if (bike.imageUrl != null && bike.imageUrl != "null" && bike.imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(bike.imageUrl),
                    contentDescription = "Bike Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 16.dp)
                )
            } else {
                Text(text = "No image for this bike")
            }
            Button(onClick = { nav.navigate("BikeRental/${bike.id}") }) {
                Text(text = "Show")
            }
        }
    }
}