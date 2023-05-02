package com.LaursenJessen.bikeshare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Teal200 = Color(0xFF03DAC5)
val Teal700 = Color(0xFF018786)
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Grey50 = Color(0xFFF9FAFB)
val Grey800 = Color(0xFF424242)
val Grey900 = Color(0xFF212121)
val smokeyWhite = Color(0xFFF4F4F4)
val brownSugar = Color(0xFFC57B57)
val darkerBrownSugar = Color(0xFF894D2F)

val LightColorPalette = lightColors(
    primary = brownSugar,
    primaryVariant = darkerBrownSugar,
    secondary = Purple500,
    background = Grey50,
    surface = smokeyWhite,
    onPrimary = smokeyWhite,
    onSecondary = smokeyWhite,
    onBackground = Grey900,
    onSurface = Grey900,
)

val DarkColorPalette = darkColors(
    primary = brownSugar,
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