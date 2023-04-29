package com.LaursenJessen.bikeshare.firestore

data class Bike(
    val id:String,
    val address:String,
    val description:String,
    val manufacturer:String,
    val model:String,
    val rentedOut:Boolean,
    val userId:String,
    val imageUrl:String
)