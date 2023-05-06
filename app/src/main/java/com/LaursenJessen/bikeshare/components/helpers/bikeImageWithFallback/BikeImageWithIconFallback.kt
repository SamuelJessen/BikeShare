package com.LaursenJessen.bikeshare.components.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter

@Composable
fun BikeImageWithIconFallback(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp
) {
    val painter = rememberImagePainter(data = imageUrl, builder = {
        crossfade(true)
    })

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .matchParentSize()
                .alpha(if (painter.state is ImagePainter.State.Success) 1f else 0f),
        )

        Icon(
            imageVector = Icons.Filled.DirectionsBike,
            contentDescription = contentDescription,
            modifier = Modifier
                .alpha(if (painter.state is ImagePainter.State.Success) 0f else 1f)
                .size(iconSize)
                .align(Alignment.Center),
        )
    }
}