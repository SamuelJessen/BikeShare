package com.laursenjessen.bikeshare.components.drawermenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laursenjessen.bikeshare.components.drawermenu.models.DrawerMenuItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerMenuItem(
    menuItems: List<DrawerMenuItemModel>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    LazyColumn(modifier) {
        items(menuItems) { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    item.onClick()
                    scope.launch { scaffoldState.drawerState.close() }
                }
                .padding(16.dp)) {
                Icon(
                    imageVector = item.iconVector, contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title, style = itemTextStyle, modifier = Modifier.weight(1f)
                )
            }
        }
    }
}