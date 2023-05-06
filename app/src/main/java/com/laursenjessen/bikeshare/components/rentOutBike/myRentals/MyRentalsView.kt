package com.laursenjessen.bikeshare.components.rentOutBike.myRentals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.laursenjessen.bikeshare.services.firestore.FireStore
import com.laursenjessen.bikeshare.services.firestore.models.Rental
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun MyRentalsView(service: FireStore, nav: NavController) {
    val rentalList = remember { mutableStateOf(emptyList<Rental>()) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(true) }

    val userId = service.auth.uid ?: ""

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            rentalList.value = service.getRentals(userId)
            isLoading.value = false
        }
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            coroutineScope.launch {
                swipeRefreshState.isRefreshing = true
                if (userId.isNotEmpty()) {
                    rentalList.value = service.getRentals(userId)
                }
                swipeRefreshState.isRefreshing = false
            }
        },
    ) {
        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (rentalList.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("You currently have no rentals", style = MaterialTheme.typography.h6)
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Rentals on your bikes",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(10.dp)
                    )
                    Button(onClick = { nav.navigate("MyBikesView") }) {
                        Text(text = "Your bikes")
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Divider(thickness = 3.dp)
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    itemsIndexed(rentalList.value) { _, rental ->
                        RentalListItem(rental = rental)
                    }
                }
            }
        }
    }
}

