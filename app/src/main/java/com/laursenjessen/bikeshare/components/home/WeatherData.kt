package com.laursenjessen.bikeshare.components.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laursenjessen.bikeshare.services.position.PositionService
import com.laursenjessen.bikeshare.services.weather.KtorWeatherService
import com.laursenjessen.bikeshare.services.weather.models.WeatherData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WeatherView(locationService : PositionService) {
    val weatherService = KtorWeatherService()
    val data = remember { mutableStateOf<WeatherData?>(null) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(locationService.locationOn.value) {
        if (!locationService.locationOn.value) {
            locationService.requestPermission()
        }
        val location = locationService.getCurrentLocation()
        isLoading.value = true
        data.value = weatherService.get(location.latitude.toString(),location.longitude.toString())
        Log.d("WeatherData",data.value.toString())
        isLoading.value = false
    }
    DisposableEffect(Unit) { onDispose { weatherService.close() } }
    Box(
    ) {
        if (isLoading.value) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    "Loading weather data...",
                    Modifier.padding(top = 8.dp)
                )
            }
        } else {
            data.value?.let { data ->
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text(
                        text = "Current weather:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.laursenjessen.bikeshare.R.drawable.weather_ic),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "${data.current_weather.temperature}°C",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Wind speed: ${data.current_weather.windspeed} km/h",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
