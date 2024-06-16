package data

actual class PlatformProviders {
    actual fun intentHandler(): IntentHandler {
        return IntentHandler()
    }
}