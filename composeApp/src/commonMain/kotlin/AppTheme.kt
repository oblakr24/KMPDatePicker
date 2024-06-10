import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Grey20,
    onPrimaryContainer = Blue90,
    inversePrimary = Blue40,
    secondary = DarkBlue80,
    onSecondary = DarkBlue20,
    secondaryContainer = DarkBlue30,
    onSecondaryContainer = DarkBlue90,
    tertiary = Yellow80,
    onTertiary = Yellow20,
    tertiaryContainer = Yellow30,
    onTertiaryContainer = Yellow90,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    inversePrimary = Blue80,
    secondary = DarkBlue40,
    onSecondary = Color.White,
    secondaryContainer = DarkBlue90,
    onSecondaryContainer = DarkBlue10,
    tertiary = Yellow40,
    onTertiary = Color.White,
    tertiaryContainer = Yellow90,
    onTertiaryContainer = Yellow10,
)

@Composable
fun AppTheme(
    overrideDarkMode: Boolean? = null,
    darkTheme: Boolean = overrideDarkMode ?: isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val rememberedColors = remember(darkTheme) {
        when {
            darkTheme -> darkDatePickerColors()
            else -> lightDatePickerColors()
        }
    }
    CompositionLocalProvider(
        LocalDatePickerColors provides rememberedColors,
    content = {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    })
}
