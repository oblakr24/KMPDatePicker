import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import di.ComponentCreator
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val componentCreator = ComponentCreator()
    val component = componentCreator.createAppComponent()
    ComposeViewport(document.body!!) {
        // FIXME: Has an issue: https://slack-chats.kotlinlang.org/t/16227323/hi-i-have-a-sample-wasmjs-app-uses-few-dom-api-everything-co
        val currentPath = window.location.pathname.removePrefix("/")
        val pathInput = PathInput.resolveFrom(currentPath)
        App(pathInput = pathInput, component = component)
    }
}