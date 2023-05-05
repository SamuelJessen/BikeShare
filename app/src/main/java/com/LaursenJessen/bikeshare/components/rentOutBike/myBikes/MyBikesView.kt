package com.LaursenJessen.bikeshare.components.rentOutBike.myBikes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.models.Bike

@Composable
fun MyBikesView(service: FireStore, nav: NavController) {
    val bikes = remember { mutableStateOf(emptyList<Bike>()) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        bikes.value = service.getBikes()
    }

    LaunchedEffect(Unit) {
        bikes.value = service.getBikes()
    }

    val bikesForUser = bikes.value.filter { it.userId == service.auth.uid }
    
    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "My Bikes",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { nav.navigate("AddBike") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize(), state = listState) {
            items(bikesForUser) { bike ->
                BikeListItem(bike, nav)
            }
        }
    }
}

@Composable
fun BikeListItem(bike: Bike, nav: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = { nav.navigate("MyBikeView/${bike.id}") }),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Log.d("Bike image url: ", bike.imageUrl)
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            BikeImageWithIconFallback(
                imageUrl = bike.imageUrl,
                contentDescription = "Bike image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                iconSize = 70.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = bike.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun BikeImageWithIconFallback(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp
) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
        }
    )

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



