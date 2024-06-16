package data

import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

actual class PlatformProviders(private var activityRef: WeakReference<ComponentActivity>?) {
    actual fun intentHandler(): IntentHandler {
        return IntentHandler(activityRef)
    }
}