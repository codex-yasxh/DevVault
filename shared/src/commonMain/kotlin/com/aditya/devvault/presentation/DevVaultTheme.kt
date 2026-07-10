package com.aditya.devvault.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DevVaultPrimary = Color(0xFF1A73E8)
private val DevVaultOnPrimary = Color.White
private val DevVaultPrimaryContainer = Color(0xFFD3E3FD)
private val DevVaultOnPrimaryContainer = Color(0xFF041E49)

private val DevVaultSecondary = Color(0xFF545F71)
private val DevVaultOnSecondary = Color.White
private val DevVaultSecondaryContainer = Color(0xFFD8E3F8)
private val DevVaultOnSecondaryContainer = Color(0xFF111C2B)

private val DevVaultTertiary = Color(0xFF6E5676)
private val DevVaultError = Color(0xFFBA1A1A)
private val DevVaultOnError = Color.White
private val DevVaultErrorContainer = Color(0xFFFFDAD6)

private val DevVaultBackground = Color(0xFFFDFBFF)
private val DevVaultOnBackground = Color(0xFF1A1C1E)
private val DevVaultSurface = Color(0xFFFDFBFF)
private val DevVaultOnSurface = Color(0xFF1A1C1E)
private val DevVaultSurfaceVariant = Color(0xFFE0E3EB)
private val DevVaultOnSurfaceVariant = Color(0xFF44474F)

private val DevVaultOutline = Color(0xFF74777F)

private val DevVaultStatusBuilding = Color(0xFF1A73E8)
private val DevVaultStatusShipped = Color(0xFF1E8E3E)
private val DevVaultStatusPaused = Color(0xFFF9AB00)
private val DevVaultStatusAbandoned = Color(0xFF9AA0A6)

private val DevVaultColorScheme = lightColorScheme(
    primary = DevVaultPrimary,
    onPrimary = DevVaultOnPrimary,
    primaryContainer = DevVaultPrimaryContainer,
    onPrimaryContainer = DevVaultOnPrimaryContainer,
    secondary = DevVaultSecondary,
    onSecondary = DevVaultOnSecondary,
    secondaryContainer = DevVaultSecondaryContainer,
    onSecondaryContainer = DevVaultOnSecondaryContainer,
    tertiary = DevVaultTertiary,
    error = DevVaultError,
    onError = DevVaultOnError,
    errorContainer = DevVaultErrorContainer,
    background = DevVaultBackground,
    onBackground = DevVaultOnBackground,
    surface = DevVaultSurface,
    onSurface = DevVaultOnSurface,
    surfaceVariant = DevVaultSurfaceVariant,
    onSurfaceVariant = DevVaultOnSurfaceVariant,
    outline = DevVaultOutline
)

object DevVaultSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
}

object DevVaultColors {
    val StatusBuilding = DevVaultStatusBuilding
    val StatusShipped = DevVaultStatusShipped
    val StatusPaused = DevVaultStatusPaused
    val StatusAbandoned = DevVaultStatusAbandoned
}

@Composable
fun DevVaultTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DevVaultColorScheme,
        content = content
    )
}
