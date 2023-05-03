package com.LaursenJessen.bikeshare.components.rentbike

import android.util.Log
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
    val duration = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    LaunchedEffect(bikeId) {
        bike.value = service.getBikeById(bikeId ?: "")
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            modifier = Modifier.fillMaxWidth(),
            title = { Text(text = "Rent Bike") },
            text = {
                Column {
                    OutlinedTextField(
                        value = duration.value,
                        onValueChange = { duration.value = it },
                        label = { Text("Duration (hours)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = price.value,
                        onValueChange = { price.value = it },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneNumber.value,
                        onValueChange = { phoneNumber.value = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val rental = FirebaseAuth.getInstance().currentUser?.let {
                                Rental(
                                    id = UUID.randomUUID().toString(),
                                    bike = bike.value!!,
                                    userId = it.uid,
                                    userEmail = userEmail,
                                    bikeId = bike.value!!.id,
                                    rentDuration = duration.value,
                                    phoneNumber = phoneNumber.value
                                )
                            }
                            rentalProcess.value = rental
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
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

