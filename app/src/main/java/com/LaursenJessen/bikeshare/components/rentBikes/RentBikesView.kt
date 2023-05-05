package com.LaursenJessen.bikeshare.components.rentBikes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.models.Bike
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun RentBikeView(service: FireStore, nav: NavController) {
    val bikeList = remember { mutableStateOf(emptyList<Bike>()) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        bikeList.value = service.getBikes()
    }

    val availableBikes = bikeList.value.filter { bike ->
        !bike.rentedOut && bike.userId != service.auth.uid && bike.name.contains(
            searchQuery,
            ignoreCase = true
        )
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
        Column(modifier = Modifier.fillMaxSize()) {
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
                        text = "No bikes available for rent", style = MaterialTheme.typography.subtitle1
                    )
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp, horizontal = 0.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = 4.dp
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                        },
                        label = { Text("Search bikes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    items(availableBikes) { bike ->
                        BikeRentListItem(bike = bike, nav = nav)
                    }
                }
            }
        }
    }
}
