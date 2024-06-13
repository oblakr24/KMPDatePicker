import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        val currentPath = window.location.pathname.removePrefix("/")
        App(pathInput = PathInput.resolveFrom(currentPath))
    }
}
