package com.LaursenJessen.bikeshare.components.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Serializable
data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val current_weather: CurrentWeather,
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature") val temperature: Double,
    @SerialName("windspeed") val windspeed: Double,
    @SerialName("winddirection") val winddirection: Double,
    @SerialName("weathercode") val weathercode: Int,
    @SerialName("is_day") val is_day: Int,
    @SerialName("time") val time: String,

    )

interface WeatherService{
    suspend fun get(lat: String, longit: String): WeatherData?
}

const val baseUrlPost = "https://api.open-meteo.com/v1/forecast"

class KtorWeatherService : WeatherService {
    private val client = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    override suspend fun get(lat: String,longit: String): WeatherData? {
        return try {
            client.get {
                url(baseUrlPost)
                parameter("latitude",lat)
                parameter("longitude",longit)
                parameter("timezone","Europe/Berlin")
                parameter("current_weather",true)
                /*Log.e("url",client.get() {
                    url("$baseUrlPost")
                    parameter("latitude",lat)
                    parameter("longitude",longit)
                    parameter("timezone","Europe/Berlin")
                    parameter("current_weather",true)
                }.body<String>().toString())*/
            }.body()
        } catch (e: Exception) {
            Log.v("WEATHER SERVICE", e.toString())
            null
        }
    }

    fun close() {
        client.close()
    }
}


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
        Log.e("WeatherData",data.value.toString())
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
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.LaursenJessen.bikeshare.R.drawable.weather_ic),
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




class PositionService(
    private val client: FusedLocationProviderClient,
    private val activity: Activity,
)
{
    val locationOn = mutableStateOf(false)

    companion object {
        const val REQUEST_ID = 9
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun getCurrentLocation(): Location {
        return suspendCoroutine { continuation ->
            try {
                client.getCurrentLocation(
                    LocationRequest.QUALITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener {
                    continuation.resume(it)
                }.addOnFailureListener {
                    Log.v(this.javaClass.name, "Location request failure")
                    requestPermission()
                }
            } catch (e: SecurityException) {
                Log.v(this.javaClass.name, "Location request not allowed")
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (!checkPermission()) {
            locationOn.value = false
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ID
            )
        } else {
            locationOn.value = true
        }
    }
}

