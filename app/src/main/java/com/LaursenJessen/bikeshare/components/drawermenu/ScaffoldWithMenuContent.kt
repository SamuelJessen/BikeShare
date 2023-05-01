package com.LaursenJessen.bikeshare.components.drawermenu

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.LaursenJessen.bikeshare.components.drawermenu.models.DrawerMenuItem
import com.LaursenJessen.bikeshare.components.drawermenu.models.DrawerMenuItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldWithMenuContent(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    menuItems: List<DrawerMenuItemModel>,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "BikeShare") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                }
            )
        },
        drawerContent = {
            DrawerMenuHeader()
            DrawerMenuItem(menuItems, scaffoldState = scaffoldState, scope = scope)
        },
        content = { content() }
    )
}