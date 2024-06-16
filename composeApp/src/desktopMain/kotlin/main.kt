import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.createAppComponent

fun main() = application {
    val appComponent = createAppComponent()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPDatePicker",
    ) {
        App(component = appComponent)
    }
}