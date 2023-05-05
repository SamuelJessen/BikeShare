package com.LaursenJessen.bikeshare.components.rentOutBike.myBikes.myBike

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.services.firestore.FireStore
import com.LaursenJessen.bikeshare.services.firestore.models.Bike
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditBikeView(service: FireStore, nav: NavController) {
    val bikeId = nav.currentBackStackEntry?.arguments?.getString("bikeId")
    val bike = remember { mutableStateOf<Bike?>(null) }
    val name = remember { mutableStateOf("") }
    val dailyPrice = remember { mutableStateOf("") }
    val distance = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val rentedOut = remember { mutableStateOf(false) }

    // Fetch the bike data from Firestore
    LaunchedEffect(bikeId) {
        val fetchedBike = service.getBikeById(bikeId ?: "")
        if (fetchedBike != null) {
            bike.value = fetchedBike
            name.value = fetchedBike.name
            dailyPrice.value = fetchedBike.dailyPrice.toString()
            distance.value = fetchedBike.distance.toString()
            address.value = fetchedBike.address
            description.value = fetchedBike.description
            rentedOut.value = fetchedBike.rentedOut
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Edit Bike",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = dailyPrice.value,
            onValueChange = { dailyPrice.value = it },
            label = { Text(text = "Price pr day") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = distance.value,
            onValueChange = { distance.value = it },
            label = { Text(text = "Preliminary ride distance (km)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = address.value,
            onValueChange = { address.value = it },
            label = { Text(text = "Address") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text(text = "Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Available for rent")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = !rentedOut.value,
                onCheckedChange = { rentedOut.value = !it },
                modifier = Modifier.alignByBaseline(),
            )
        }

        Button(
            onClick = {
                bike?.let {
                    val updatedBike = it.value?.copy(
                        name = name.value,
                        dailyPrice = dailyPrice.value.toDouble().toInt(),
                        distance = distance.value.toDouble().toInt(),
                        address = address.value,
                        description = description.value,
                        rentedOut = rentedOut.value
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        if (updatedBike != null) {
                            service.updateBike(updatedBike)
                        }
                    }
                    nav.navigate("MyBikesView")
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Save Changes")
        }
    }
}