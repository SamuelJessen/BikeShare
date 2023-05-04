package com.LaursenJessen.bikeshare.components.rentOutBike.myBikes.myBike

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.models.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyBikeView(
    service: FireStore,
    nav: NavController
) {
    val bikeId = nav.currentBackStackEntry?.arguments?.getString("bikeId")
    val bike = remember { mutableStateOf<Bike?>(null) }
    LaunchedEffect(bikeId) {
        bike.value = service.getBikeById(bikeId ?: "")
    }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(text = "Delete bike") },
            text = {
                Text(
                    text = "Are you sure you want to delete ${bike.value?.name}?",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (bikeId != null) {
                            service.deleteBike(bikeId)
                        }
                    }
                    nav.popBackStack()
                    showConfirmationDialog = false
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        bike.value?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(it.imageUrl),
                    contentDescription = "Bike image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.height(16.dp))
                it.name.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.h4
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Address: ${it.address}",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Description: ${it.description}",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Distance: ${it.distance}",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rented Out: ${it.rentedOut}",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            showConfirmationDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Delete")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            nav.navigate("EditBike")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}