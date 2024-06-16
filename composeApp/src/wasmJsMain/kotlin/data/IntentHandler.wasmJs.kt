package data

import kotlinx.browser.window

actual class IntentHandler {
    actual fun openURL(url: String) {
        window.location.href = url
    }
}