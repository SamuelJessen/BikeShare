package com.LaursenJessen.bikeshare.components.rentOutBike.myBikes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.components.helpers.BikeImageWithIconFallback
import com.LaursenJessen.bikeshare.services.firestore.models.Bike

@Composable
fun MyBikeListItem(bike: Bike, nav: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 4.dp)
            .clickable(onClick = { nav.navigate("MyBikeView/${bike.id}") }),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            BikeImageWithIconFallback(
                imageUrl = bike.imageUrl,
                contentDescription = "Bike image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                iconSize = 70.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = bike.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
        }
    }
}