package com.LaursenJessen.bikeshare.components.rentBikes

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun GoogleMapsLocationButton(address: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uriString = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
    intent.setPackage("com.google.android.apps.maps")

    Button(onClick = {
        coroutineScope.launch {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
                context.startActivity(browserIntent)
            }
        }
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Location (Google Maps)")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.Map, contentDescription = null)
        }
    }
}

