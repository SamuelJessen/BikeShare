package com.laursenjessen.bikeshare.components.rentOutBike.addBikes

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.laursenjessen.bikeshare.services.firestore.FireStore
import com.laursenjessen.bikeshare.services.firestore.models.Bike
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
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val loading = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImage = uri
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(value = dailyPrice.value,
                    onValueChange = { dailyPrice.value = it },
                    label = { Text(text = "Price pr day") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = distance.value,
                    onValueChange = { distance.value = it },
                    label = { Text(text = "Preliminary ride distance (km)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = address.value,
                    onValueChange = { address.value = it },
                    label = { Text(text = "Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text(text = "Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                ImageContent(selectedImage) {
                    launcher.launch("image/*")
                }
                if (selectedImage != null) {
                    Text(
                        text = "Press image to change",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.caption.copy(
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    )
                }

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
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = Color.Green.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.Red,
                            uncheckedTrackColor = Color.Red.copy(alpha = 0.5f)
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (loading.value) {
                            CircularProgressIndicator()
                        } else {
                            Button(
                                onClick = {
                                    loading.value = true
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
                                    val storageRef = storage.reference.child("images/${bike.id}")
                                    selectedImage?.let { uri ->
                                        storageRef.putFile(uri).addOnSuccessListener {
                                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                bike.imageUrl = uri.toString()

                                                CoroutineScope(Dispatchers.IO).launch {
                                                    service.addBike(bike)
                                                }
                                                nav.navigate("MyBikesView")
                                            }
                                        }.addOnFailureListener { exception ->
                                            Log.e(
                                                "AddBikeView",
                                                "Error uploading image to Firebase Storage: $exception"
                                            )
                                        }
                                    } ?: run {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            service.addBike(bike)
                                        }
                                        nav.navigate("MyBikesView")
                                        loading.value = false
                                    }
                                },
                            ) {
                                Text(text = "Add bike")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageContent(selectedImage: Uri? = null, onImageClick: () -> Unit) {
    Box(
        modifier = Modifier.height(
            if (selectedImage != null) {
                200.dp
            } else {
                80.dp
            }
        ), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedImage != null) {
                Image(painter = rememberImagePainter(selectedImage),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onImageClick()
                        })
            } else Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onImageClick, modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        text = "Upload bike image", fontSize = 16.sp
                    )
                }
            }
        }
    }
}