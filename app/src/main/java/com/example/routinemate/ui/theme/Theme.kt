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

    // 메인 색상
    primary = RoutinePrimary,
    onPrimary = RoutineOnPrimary,

    // 강조 카드 / 선택 영역
    primaryContainer = RoutinePrimaryContainer,
    onPrimaryContainer = RoutineOnPrimaryContainer,

    // 보조 색상
    secondary = RoutineSecondary,
    onSecondary = RoutineOnSecondary,

    // 보조 카드 / 영역
    secondaryContainer = RoutineSecondaryContainer,
    onSecondaryContainer = RoutineOnSecondaryContainer,

    // 앱 전체 배경
    background = RoutineBackground,
    onBackground = RoutineOnBackground,

    // 카드 등의 Surface
    surface = RoutineSurface,
    onSurface = RoutineOnSurface,

    // 보조 Surface
    surfaceVariant = RoutineSurfaceVariant,
    onSurfaceVariant = RoutineOnSurfaceVariant,

    // 테두리
    outline = RoutineOutline,

    // 오류
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

        // Android 12 이상에서 Dynamic Color 사용
        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        // Dark Theme
        darkTheme -> DarkColorScheme

        // Light Theme
        else -> LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {

        val window = (view.context as Activity).window

        // 상태바 배경
        window.statusBarColor =
            colorScheme.background.toArgb()

        // 하단 시스템 네비게이션바 배경
        window.navigationBarColor =
            colorScheme.surface.toArgb()

        WindowCompat
            .getInsetsController(window, view)
            .apply {

                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RoutineShapes,
        content = content
    )
}