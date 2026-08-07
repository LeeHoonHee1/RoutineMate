package com.example.routinemate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RoutinePrimary,
    onPrimary = RoutineOnPrimary,
    primaryContainer = RoutinePrimaryContainer,
    onPrimaryContainer = RoutineOnPrimaryContainer,

    secondary = RoutineSecondary,
    onSecondary = RoutineOnSecondary,
    secondaryContainer = RoutineSecondaryContainer,
    onSecondaryContainer = RoutineOnSecondaryContainer,

    background = RoutineBackground,
    onBackground = RoutineOnBackground,

    surface = RoutineSurface,
    onSurface = RoutineOnSurface,
    surfaceVariant = RoutineSurfaceVariant,
    onSurfaceVariant = RoutineOnSurfaceVariant,

    outline = RoutineOutline,

    error = RoutineError,
    onError = RoutineOnPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = RoutinePrimaryDark,
    onPrimary = RoutineOnPrimaryDark,
    primaryContainer = RoutinePrimaryContainerDark,
    onPrimaryContainer = RoutineOnPrimaryContainerDark,

    secondary = RoutineSecondaryDark,
    onSecondary = RoutineOnSecondaryDark,
    secondaryContainer = RoutineSecondaryContainerDark,
    onSecondaryContainer = RoutineOnSecondaryContainerDark,

    background = RoutineBackgroundDark,
    onBackground = RoutineOnBackgroundDark,

    surface = RoutineSurfaceDark,
    onSurface = RoutineOnSurfaceDark,
    surfaceVariant = RoutineSurfaceVariantDark,
    onSurfaceVariant = RoutineOnSurfaceVariantDark,

    outline = RoutineOutlineDark,

    error = RoutineErrorDark,
    onError = RoutineOnPrimaryDark
)

@Composable
fun RoutineMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (view.context as Activity).window

        window.statusBarColor = colorScheme.background.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !darkTheme
            isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}