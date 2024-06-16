import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.ComponentCreator

fun main() = application {
    val componentCreator = ComponentCreator()
    val appComponent = componentCreator.createAppComponent()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPDatePicker",
    ) {
        App(component = appComponent)
    }
}