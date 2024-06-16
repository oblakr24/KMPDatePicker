import androidx.compose.ui.window.ComposeUIViewController
import di.AppComponent

fun MainViewController(component: AppComponent) = ComposeUIViewController { App(component = component) }