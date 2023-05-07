package com.laursenjessen.bikeshare.components.rentOutBike.myBikes

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun ImageContent(selectedImage: Uri? = null, onImageClick: () -> Unit) {
    Box(
        modifier = Modifier.height(
            if (selectedImage != null) {
                200.dp
            } else {
                80.dp
            }
        ), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedImage != null) {
                Image(painter = rememberImagePainter(selectedImage),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onImageClick()
                        })
            } else Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onImageClick, modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        text = "Upload bike image", fontSize = 16.sp
                    )
                }
            }
        }
    }
}