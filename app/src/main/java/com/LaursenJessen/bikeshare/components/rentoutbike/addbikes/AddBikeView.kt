package com.LaursenJessen.bikeshare.components.rentoutbike.addbikes

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.firestore.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AddBikeView(service: FireStore, nav: NavController) {
    val name = remember { mutableStateOf("") }
    val priceHour = remember { mutableStateOf("") }
    val distance = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val rentedOut = remember { mutableStateOf(false) }
    val image = remember { mutableStateOf<Bitmap?>(null)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Add Bike",
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
            value = priceHour.value,
            onValueChange = { priceHour.value = it },
            label = { Text(text = "Price pr day") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = distance.value,
            onValueChange = { distance.value = it },
            label = { Text(text = "Preliminary ride distance (km)") },
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.alignByBaseline()
            )
        }

        Button(
            onClick = {
                val bike = Bike(
                    id = UUID.randomUUID().toString(),
                    address = address.value,
                    name = name.value,
                    dailyPrice = distance.value.toDouble().toInt(),
                    distance = distance.value.toDouble().toInt(),
                    description = description.value,
                    rentedOut = rentedOut.value,
                    userId = service.auth.uid.toString(),
                    imageUrl = ""
                )
                CoroutineScope(Dispatchers.IO).launch {
                    service.addBike(bike)
                }
                nav.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Add bike")
        }
    }
}
