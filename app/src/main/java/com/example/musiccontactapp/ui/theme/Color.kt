package com.example.musiccontactapp.ui.theme

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val Grey = Color(0xFFA5A5A5)
val DarkGrey = Color(0xFF433E48)
val Btn = Brush.linearGradient(
    colorStops = arrayOf(
        0.31f to Color(0xFF842ED8),
        0.59f to Color(0xFFDB28A9),
        1.0f to Color(0xFF9D1DCA)
    )
)
val DisableBtn = Brush.linearGradient(
    colorStops = arrayOf(
        1.0f to Color(0xFF433E48),
        1.0f to Color(0xFF433E48)
    )
)


