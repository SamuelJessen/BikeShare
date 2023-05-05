package com.LaursenJessen.bikeshare.components.rentOutBike.addBikes

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.models.Bike
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AddBikeView(service: FireStore, nav: NavController) {
    val storage = Firebase.storage
    val name = remember { mutableStateOf("") }
    val dailyPrice = remember { mutableStateOf("") }
    val distance = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val rentedOut = remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<Uri?>(null)}
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) {uri ->
        selectedImage = uri
    }
    LazyColumn(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 16.dp)){
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ImageContent(selectedImage) {
                    launcher.launch("image/*")
                }
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
                        val bike = Bike(
                            id = UUID.randomUUID().toString(),
                            address = address.value,
                            name = name.value,
                            dailyPrice = dailyPrice.value.toDouble().toInt(),
                            distance = distance.value.toDouble().toInt(),
                            description = description.value,
                            rentedOut = rentedOut.value,
                            userId = service.auth.uid.toString(),
                            imageUrl = ""
                        )

                        // Create a storage reference for the image file
                        val storageRef = storage.reference.child("images/${UUID.randomUUID()}")
                        selectedImage?.let { uri ->
                            // Upload the image data to Firebase Storage
                            storageRef.putFile(uri)
                                .addOnSuccessListener {
                                    // Get the download URL for the image
                                    storageRef.downloadUrl
                                        .addOnSuccessListener { uri ->
                                            bike.imageUrl = uri.toString()

                                            CoroutineScope(Dispatchers.IO).launch {
                                                service.addBike(bike)
                                            }
                                            nav.navigate("MyBikesView")
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("AddBikeView", "Error uploading image to Firebase Storage: $exception")
                                }
                        } ?: run {
                            CoroutineScope(Dispatchers.IO).launch {
                                service.addBike(bike)
                            }
                            nav.navigate("MyBikesView")
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Add bike")
                }
            }
        }
    }

}

@Composable
private fun ImageContent(selectedImage: Uri? = null, onImageClick: () -> Unit) {
    Box(
        Modifier.height(if(selectedImage!=null){200.dp}
        else {80.dp})) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedImage != null) {
                Image(
                    painter = rememberImagePainter(selectedImage),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onImageClick()
                        })
            } else
                OutlinedButton(onClick = onImageClick) {
                    Text(text = "Choose Image")
                }
        }
    }
}