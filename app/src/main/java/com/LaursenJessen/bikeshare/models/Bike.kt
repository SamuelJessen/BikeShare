package com.LaursenJessen.bikeshare.models

data class Bike(
    val id:String,
    val address:String,
    val description:String,
    val name:String,
    val dailyPrice: Int,
    val distance:Int,
    val rentedOut:Boolean,
    val userId:String,
    val imageUrl:String
)