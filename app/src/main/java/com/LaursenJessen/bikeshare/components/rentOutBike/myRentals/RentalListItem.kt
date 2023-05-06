package com.LaursenJessen.bikeshare.components.rentOutBike.myRentals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.LaursenJessen.bikeshare.services.firestore.models.Rental

@Composable
fun RentalListItem(rental: Rental) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = rental.bike.name, style = MaterialTheme.typography.h6)
            Text(
                text = "Rented by: ${rental.userEmail}",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = "Duration: ${rental.rentDurationDays} days",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = "Price payed: ${rental.dailyPrice} (DKK)",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}
