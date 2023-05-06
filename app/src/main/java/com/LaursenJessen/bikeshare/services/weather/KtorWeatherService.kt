package com.LaursenJessen.bikeshare.services.weather

import android.util.Log
import com.LaursenJessen.bikeshare.services.weather.models.WeatherData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface WeatherService {
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

    override suspend fun get(lat: String, longit: String): WeatherData? {
        return try {
            client.get {
                url(baseUrlPost)
                parameter("latitude", lat)
                parameter("longitude", longit)
                parameter("timezone", "Europe/Berlin")
                parameter("current_weather", true)
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