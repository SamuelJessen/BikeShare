package com.LaursenJessen.bikeshare.components.home

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(nav: NavController, positionService: PositionService) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row() {
                Text(
                    text = "Welcome to BikeShare",
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
            Row(){
                Button(
                    onClick = { nav.navigate("RentBikeView") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Rent a bike", style = MaterialTheme.typography.button
                    )
                }
            }
            Row(){
                Button(
                    onClick = { nav.navigate("MyBikesView") }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rent out my bike", style = MaterialTheme.typography.button
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(modifier = Modifier.height(0.dp))
                WeatherView(locationService = positionService)
            }
        }
    }
}
