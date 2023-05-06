package com.laursenjessen.bikeshare.services.firestore.models

data class Rental(
    val id:String,
    val bike: Bike,
    val userId:String,
    val userEmail: String,
    val bikeId:String,
    val rentDurationDays:Int,
    val dailyPrice: Int
)