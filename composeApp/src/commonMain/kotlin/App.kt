import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import data.DarkModeState
import di.AppComponent
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.main.MainComponent
import ui.main.MainScreen

@Composable
@Preview
fun App(pathInput: PathInput = PathInput.None, component: AppComponent) {
    val mainComponent = remember(pathInput) {
        MainComponent(
            pathInput = pathInput,
            storage = component.storage(),
            intentHandler = component.intentHandler()
        )
    }

    val darkTheme = mainComponent.darkMode.collectAsState().value
    val darkModeFlag = when (darkTheme) {
        DarkModeState.ON -> true
        DarkModeState.OFF -> false
        DarkModeState.FOLLOW_SYSTEM -> null
    }
    AppTheme(overrideDarkMode = darkModeFlag) {
        MainScreen(mainComponent)
    }

    DisposableEffect(Unit) {
        onDispose {
            mainComponent.onCleared()
        }
    }
}