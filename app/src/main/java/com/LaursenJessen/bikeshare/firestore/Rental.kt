package com.LaursenJessen.bikeshare.firestore

data class Rental(
    val id:String,
    val bike: Bike,
    val userId:String,
    val userEmail: String,
    val bikeId:String,
    val rentDuration:String,
    val phoneNumber:String
)