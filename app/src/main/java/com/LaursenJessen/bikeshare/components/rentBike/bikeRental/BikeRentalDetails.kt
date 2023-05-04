package com.LaursenJessen.bikeshare.components.rentBike

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.models.Bike
import com.LaursenJessen.bikeshare.firestore.models.Rental
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
    val totalPrice = derivedStateOf { duration.value.toInt() * bike.value!!.dailyPrice }

    LaunchedEffect(bikeId) {
        bike.value = service.getBikeById(bikeId ?: "")
    }

    if (showDialog.value) {
        AlertDialog(onDismissRequest = { showDialog.value = false },
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(text = "Rent ${bike.value!!.name}", style = MaterialTheme.typography.h4)
            },
            text = {
                Column {
                    Text("Price (DKK): ${totalPrice.value}", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(30.dp))
                    Text("Duration (days): ${duration.value.toInt()}")
                    Slider(
                        value = duration.value,
                        onValueChange = { duration.value = it },
                        valueRange = 1f..30f,
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = {
                                showDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.error
                            ),
                            modifier = Modifier.weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text("Close", style = MaterialTheme.typography.body1)
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
                                        dailyPrice = (duration.value.toInt() * bike.value!!.dailyPrice)
                                    )
                                }
                                rentalProcess.value = rental
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            modifier = Modifier.weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text("Confirm", style = MaterialTheme.typography.body1)
                        }
                        LaunchedEffect(rentalProcess.value) {
                            rentalProcess.value?.let { rental ->
                                try {
                                    service.addRentalDocument(rental)
                                    service.updateBikeRentedStatus(bikeId!!, rentedOut = true)
                                    showDialog.value = false
                                    rentalProcess.value = null
                                    nav.navigate("RentBikeView")
                                } catch (e: Exception) {
                                    Log.e("Error adding rental", e.toString())
                                    rentalProcess.value = null
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { },
            dismissButton = { })
    }


    bike.value?.let { bike ->
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = bike.name,
                style = MaterialTheme.typography.h4,
            )
            if (!bike.rentedOut) {
                Text(
                    text = "Status: Available",
                    style = MaterialTheme.typography.body1,
                    color = Color.Green,
                )
            } else {
                Text(
                    text = "Status: Rented Out",
                    style = MaterialTheme.typography.body1,
                    color = Color.Red,
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
            Text(text = "${bike.description}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Preliminary ride distance: ${bike.distance}Km",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(40.dp))
            if (bike.address.isNotEmpty() && bike.address != "null" && bike.address != null) {
                GoogleMapsLocationButton(bike.address)
            } else {
                Text(text = "No address for this bike")
            }
            Button(
                onClick = { showDialog.value = true }, modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Rent this bike")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Filled.DirectionsBike, contentDescription = "rent bike icon")
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

