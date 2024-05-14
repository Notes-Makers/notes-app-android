package com.notesmakers.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * primary: Główny kolor interfejsu, często używany do elementów akcentujących, takich jak przyciski lub linki.
 * onPrimary: Kolor tekstu i ikon na tle głównego koloru (primary).
 * primaryContainer: Kolor tła dla większych powierzchni lub kontenerów, które mają wyróżniać się głównym kolorem.
 * onPrimaryContainer: Kolor tekstu i ikon na tle koloru kontenera głównego (primaryContainer).
 * inversePrimary: Kolor odwrotny do głównego, używany na elementach takich jak tekst lub ikony na tle odwrotnym do głównego koloru.
 * secondary: Drugi wiodący kolor interfejsu, używany do mniejszych akcentów i elementów wyróżniających.
 * onSecondary: Kolor tekstu i ikon na tle drugiego wiodącego koloru (secondary).
 * secondaryContainer: Kolor tła dla większych powierzchni lub kontenerów wyróżniających się drugim wiodącym kolorem.
 * onSecondaryContainer: Kolor tekstu i ikon na tle koloru kontenera drugiego wiodącego koloru (secondaryContainer).
 * tertiary: Trzeci wiodący kolor, używany rzadziej niż primary i secondary, ale nadal służący do akcentów.
 * onTertiary: Kolor tekstu i ikon na tle trzeciego wiodącego koloru (tertiary).
 * tertiaryContainer: Kolor tła dla większych powierzchni lub kontenerów wyróżniających się trzecim wiodącym kolorem.
 * onTertiaryContainer: Kolor tekstu i ikon na tle koloru kontenera trzeciego wiodącego koloru (tertiaryContainer).
 * background: Kolor tła dla całego interfejsu.
 * onBackground: Kolor tekstu i ikon na tle koloru tła (background).
 * surface: Kolor powierzchni dla kart, list i innych elementów UI.
 * onSurface: Kolor tekstu i ikon na tle powierzchni (surface).
 * surfaceVariant: Odmiana koloru powierzchni, używana do rozróżnienia różnych sekcji lub elementów.
 * onSurfaceVariant: Kolor tekstu i ikon na tle powierzchni wariantu (surfaceVariant).
 * surfaceTint: Kolor używany do nadania odcienia powierzchniom, często jako subtelny akcent.
 * inverseSurface: Kolor odwrotny do koloru powierzchni, używany na elementach takich jak tekst lub ikony na tle odwrotnym do powierzchni.
 * inverseOnSurface: Kolor tekstu i ikon na tle odwrotnej powierzchni (inverseSurface).
 * error: Kolor błędu, używany do wskazywania błędów i problemów.
 * onError: Kolor tekstu i ikon na tle koloru błędu (error).
 * errorContainer: Kolor tła dla komunikatów o błędach lub sekcji błędów.
 * onErrorContainer: Kolor tekstu i ikon na tle kontenera błędów (errorContainer).
 * outline: Kolor konturów i obramowań elementów.
 * outlineVariant: Odmiana koloru konturów, używana do rozróżnienia różnych sekcji lub elementów.
 * scrim: Kolor używany do maskowania tła lub jako półprzezroczysty overlay.
 * surfaceBright: Jaśniejszy kolor powierzchni, używany do wyróżnienia jasnych sekcji.
 * surfaceContainer: Kolor powierzchni dla kontenerów.
 * surfaceContainerHigh: Jaśniejsza wersja koloru powierzchni dla kontenerów.
 * surfaceContainerHighest: Najjaśniejsza wersja koloru powierzchni dla kontenerów.
 * surfaceContainerLow: Ciemniejsza wersja koloru powierzchni dla kontenerów.
 * surfaceContainerLowest: Najciemniejsza wersja koloru powierzchni dla kontenerów.
 * surfaceDim: Ciemniejszy kolor powierzchni, używany do wyróżnienia ciemnych sekcji.
 */

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF131313),
    onPrimary = Color(0xFFfafafa),
    primaryContainer = Color(0xFF424242),
    onPrimaryContainer = Color(0xFFfafafa),
    secondary = Color(0xFF424242),
    background = Color(0xFF212121),
    onBackground = Color(0xFFFFFFFF),
    tertiary = Color(0xFFeeeeee),
    tertiaryContainer = Color(0xFF131313),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF212121),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF424242),
    secondary = Color(0xFFe0e0e0),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF212121),
    tertiary = Color(0xFFeeeeee),
    tertiaryContainer = Color(0xFFeeeeee),
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun NoteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
