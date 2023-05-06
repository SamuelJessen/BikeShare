package com.laursenjessen.bikeshare.components.drawermenu.models

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerMenuItemModel(
    val id: String,
    val title: String,
    val iconVector: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
)