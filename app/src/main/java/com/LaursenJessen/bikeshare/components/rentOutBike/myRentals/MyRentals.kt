package com.LaursenJessen.bikeshare.components.rentOutBike.myRentals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.models.Rental
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
                Text("No one wanted to rent your bikes", style = MaterialTheme.typography.h6)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                itemsIndexed(rentalList.value) { _, rental ->
                    RentalListItem(rental = rental, nav = nav)
                }
            }
        }
    }
}

@Composable
fun RentalListItem(rental: Rental, nav: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = rental.bike.name, style = MaterialTheme.typography.h6)
            Text(text = "Rented by: ${rental.userEmail}", style = MaterialTheme.typography.subtitle1)
            Text(text = "Duration: ${rental.rentDurationDays} days", style = MaterialTheme.typography.subtitle1)
            Text(text = "Price: ${rental.dailyPrice} per day", style = MaterialTheme.typography.subtitle1)
        }
    }
}
