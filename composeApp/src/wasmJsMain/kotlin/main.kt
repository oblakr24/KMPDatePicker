import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import di.createAppComponent
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    val appComponent = createAppComponent()
    ComposeViewport(document.body!!) {
        // FIXME: Has an issue: https://slack-chats.kotlinlang.org/t/16227323/hi-i-have-a-sample-wasmjs-app-uses-few-dom-api-everything-co
        val currentPath = window.location.pathname.removePrefix("/")
        val pathInput = PathInput.resolveFrom(currentPath)
        App(pathInput = pathInput, component = appComponent)
    }
}