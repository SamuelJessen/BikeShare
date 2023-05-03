package com.LaursenJessen.bikeshare.components.rentbike

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.Rental
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@Composable
fun BikeRentalDetails(nav: NavController, service: FireStore) {
    val bikeId = nav.currentBackStackEntry?.arguments?.getString("bikeId")
    val bike = remember { mutableStateOf<Bike?>(null) }
    val rentalProcess = remember { mutableStateOf<Rental?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val duration = remember { mutableStateOf(1f) }
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    LaunchedEffect(bikeId) {
        bike.value = service.getBikeById(bikeId ?: "")
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            modifier = Modifier.fillMaxWidth(),
            title = { Text(text = "Rent ${bike.value!!.name}", style = MaterialTheme.typography.h4) },
            text = {
                Column {
                    Text("Duration (days): ${duration.value.toInt()}")
                    Slider(
                        value = duration.value,
                        onValueChange = { duration.value = it },
                        valueRange = 1f..30f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    val totalPrice = derivedStateOf { duration.value.toInt() * bike.value!!.dailyPrice }
                    Text("Price (DKK): ${totalPrice.value}", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (bikeId != null) {
                        Text(text = bikeId)
                    }
                    Button(
                        onClick = {
                            val rental = FirebaseAuth.getInstance().currentUser?.let {
                                Rental(
                                    id = UUID.randomUUID().toString(),
                                    bike = bike.value!!,
                                    userId = it.uid,
                                    userEmail = userEmail,
                                    bikeId = bike.value!!.id,
                                    rentDurationDays = duration.value.toInt(),
                                    price = (duration.value.toInt() * bike.value!!.dailyPrice)
                                )
                            }
                            rentalProcess.value = rental
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text("Confirm")
                    }
                    LaunchedEffect(rentalProcess.value) {
                        rentalProcess.value?.let { rental ->
                            try {
                                service.addRentalDocument(rental)
                                showDialog.value = false
                                rentalProcess.value = null
                                nav.navigate("RentBikeView")
                            } catch (e: Exception) {
                                Log.e("Error adding rental", e.toString())
                                rentalProcess.value = null
                            }
                        }
                    }
                    Button(
                        onClick = {
                            showDialog.value = false
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                    ) {
                        Text("Close")
                    }

                }
            },
            confirmButton = { },
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
                if (bike.address.isNotEmpty() && bike.address != "null" && bike.address != null)
                {OpenGoogleMapsButton(bike.address)}
                else {
                    Text(text = "No address for this bike")
                }

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

