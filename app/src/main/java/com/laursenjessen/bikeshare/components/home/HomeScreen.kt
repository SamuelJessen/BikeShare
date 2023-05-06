package com.laursenjessen.bikeshare.components.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.laursenjessen.bikeshare.services.position.PositionService

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(nav: NavController, positionService: PositionService) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BikeShare",
                    style = MaterialTheme.typography.h3,
                )
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    Icons.Filled.DirectionsBike,
                    contentDescription = "HomeScreenIcon",
                    modifier = Modifier.size(48.dp) // Adjust the size as needed
                )
            }
            Button(
                onClick = { nav.navigate("RentBikeView") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(bottom = 30.dp, end = 30.dp, start = 30.dp)
            ) {
                Text(
                    text = "Rent a bike", style = MaterialTheme.typography.button, fontSize = 18.sp
                )
            }
            Button(
                onClick = { nav.navigate("MyBikesView") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(bottom = 30.dp, end = 30.dp, start = 30.dp)
            ) {
                Text(
                    text = "Rent my bike", style = MaterialTheme.typography.button, fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            WeatherView(locationService = positionService)
        }
    }
}

