package com.LaursenJessen.bikeshare.services.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    @SerialName("temperature") val temperature: Double,
    @SerialName("windspeed") val windspeed: Double,
    @SerialName("winddirection") val winddirection: Double,
    @SerialName("weathercode") val weathercode: Int,
    @SerialName("is_day") val is_day: Int,
    @SerialName("time") val time: String,
)