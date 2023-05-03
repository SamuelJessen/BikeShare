package com.LaursenJessen.bikeshare.components.rentbike

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


@Composable
fun OpenGoogleMapsButton(address: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uriString = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
    intent.setPackage("com.google.android.apps.maps")

    Button(
        onClick = {
            coroutineScope.launch {
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
                    context.startActivity(browserIntent)
                }
            }
        }
    ) {
        Text(text = "View location on Google Maps")
    }
}

