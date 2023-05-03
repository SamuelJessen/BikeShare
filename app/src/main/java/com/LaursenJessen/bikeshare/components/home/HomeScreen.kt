package com.LaursenJessen.bikeshare.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(nav: NavController, auth: FirebaseAuth) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to BikeShare",
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        auth.currentUser?.let {
            Text(
                text = it.uid,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = { nav.navigate("RentBikeView") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Rent a bike",
                style = MaterialTheme.typography.button
            )
        }
        Button(
            onClick = { nav.navigate("MyBikesView") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Rent out my bike / My bikes",
                style = MaterialTheme.typography.button
            )
        }
    }
}
