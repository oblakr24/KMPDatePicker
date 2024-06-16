package di

import android.content.Context
import androidx.activity.ComponentActivity
import data.PlatformProviders
import java.lang.ref.WeakReference

actual class ComponentCreator(
    private val appContext: Context,
    private var activityRef: WeakReference<ComponentActivity>?) {
    actual fun createAppComponent(): AppComponent {
        return AppComponent(
            platformProviders = PlatformProviders(activityRef)
        )
    }
}