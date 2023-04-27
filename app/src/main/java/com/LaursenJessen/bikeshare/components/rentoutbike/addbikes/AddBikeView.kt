package com.LaursenJessen.bikeshare.components.rentoutbike.addbikes

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AddBikeView(nav: NavController) {
    Column() {
        Text("Add Bike View with field to input")
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Save")
        }
    }
}