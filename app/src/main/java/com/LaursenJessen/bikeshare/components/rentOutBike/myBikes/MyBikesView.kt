package com.LaursenJessen.bikeshare.components.rentOutBike.myBikes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.firestore.models.Bike


@Composable
fun MyBikesView(service: FireStore, nav: NavController) {
    val bikes = remember { mutableStateOf(emptyList<Bike>()) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        val list = service.getBikes()
        bikes.value = list
    }

    LaunchedEffect(Unit) {
        bikes.value = service.getBikes()
    }

    val bikesForUser = bikes.value.filter { it.userId == service.auth.uid }
    
    Column(modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { nav.navigate("AddBike") },
            shape = MaterialTheme.shapes.medium,
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Add bike",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(17.dp),
                    color = MaterialTheme.colors.background
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.padding(17.dp))
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize().padding(horizontal = 0.dp), state = listState) {
            items(bikesForUser) { bike ->
                MyBikeListItem(bike, nav)
            }
        }
    }
}





