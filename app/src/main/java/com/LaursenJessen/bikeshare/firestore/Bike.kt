package com.LaursenJessen.bikeshare.firestore

import android.os.Parcelable

data class Bike(
    val id:String,
    val address:String,
    val description:String,
    val name:String,
    val distance:Int,
    val rentedOut:Boolean,
    val userId:String,
    val imageUrl:String
)