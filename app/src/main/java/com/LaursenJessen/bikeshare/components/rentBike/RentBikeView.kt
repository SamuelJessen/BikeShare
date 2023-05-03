package com.LaursenJessen.bikeshare.components.rentBike

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.models.Bike
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@Composable
fun RentBikeView(service: FireStore, nav: NavController) {
    val bikeList = remember { mutableStateOf(emptyList<Bike>()) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        bikeList.value = service.getBikes()
    }

    val availableBikes = bikeList.value.filter { bike ->
        !bike.rentedOut && bike.userId != service.auth.uid
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            coroutineScope.launch {
                swipeRefreshState.isRefreshing = true
                bikeList.value = service.getBikes()
                swipeRefreshState.isRefreshing = false
            }
        },
    ) {
        if (bikeList.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (availableBikes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No available bikes", style = MaterialTheme.typography.subtitle1
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                items(availableBikes) { bike ->
                    BikeListItem(bike = bike, nav = nav)
                }
            }
        }
    }
}