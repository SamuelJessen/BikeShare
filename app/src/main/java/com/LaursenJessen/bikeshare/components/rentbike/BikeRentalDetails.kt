package com.LaursenJessen.bikeshare.components.rentbike

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore
import androidx.compose.material.OutlinedTextField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BikeRentalDetails(nav: NavController, service: FireStore) {
    val bikeId = nav.currentBackStackEntry?.arguments?.getString("bikeId")
    val bike = remember { mutableStateOf<Bike?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val duration = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val sliderValue = remember { mutableStateOf(0f) }
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    LaunchedEffect(bikeId) {
        bike.value = service.getBikeById(bikeId ?: "")
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Rent Bike") },
            text = {
                Column {
                    OutlinedTextField(
                        value = duration.value,
                        onValueChange = { newValue -> duration.value = newValue },
                        label = { Text("Duration (hours)") },
                        keyboardType = KeyboardType.Number
                    )
                    OutlinedTextField(
                        value = price.value,
                        onValueChange = { newValue -> price.value = newValue },
                        label = { Text("Price") },
                        keyboardType = KeyboardType.Number
                    )
                    OutlinedTextField(
                        value = phoneNumber.value,
                        onValueChange = { newValue -> phoneNumber.value = newValue },
                        label = { Text("Phone Number") },
                        keyboardType = KeyboardType.Phone
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Slide to confirm")
                    Slider(
                        value = sliderValue.value,
                        onValueChange = { newValue -> sliderValue.value = newValue },
                        valueRange = 0f..1f,
                        onValueChangeFinished = {
                            if (sliderValue.value >= 0.99f) {
                                showDialog.value = false
                            }
                        }
                    )
                }
            },
            confirmButton = {},
            dismissButton = { }
        )
    }

    bike.value?.let { bike ->
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = bike.name,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 8.dp)
            )
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
            Text(text = "Description: ${bike.description}", style = MaterialTheme.typography.body1)
            Text(text = "Distance: ${bike.distance} km", style = MaterialTheme.typography.body1)
            if (!bike.rentedOut) {
                Text(
                    text = "Status: Available",
                    style = MaterialTheme.typography.body1,
                    color = Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = { showDialog.value = true },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Rent this bike")
                }
            } else {
                Text(
                    text = "Status: Rented Out",
                    style = MaterialTheme.typography.body1,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

