package com.LaursenJessen.bikeshare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val LightColorPalette = lightColors(
    primary = Grey900,
    primaryVariant = darkerBrownSugar,
    secondary = Purple500,
    background = smokeyWhite,
    surface = smokeyWhite,
    onPrimary = smokeyWhite,
    onSecondary = smokeyWhite,
    onBackground = Grey900,
    onSurface = Grey900,
)

val DarkColorPalette = darkColors(
    primary = darkBlueCustom,
    primaryVariant = darkerBrownSugar,
    secondary = Purple200,
    background = Grey900,
    surface = Grey800,
    onPrimary = smokeyWhite,
    onSecondary = smokeyWhite,
    onBackground = smokeyWhite,
    onSurface = smokeyWhite
)


@Composable
fun BikeShareTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}