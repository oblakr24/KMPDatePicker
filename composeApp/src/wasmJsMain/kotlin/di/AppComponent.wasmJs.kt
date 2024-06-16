package di

import data.PlatformProviders

actual class ComponentCreator {
    actual fun createAppComponent(): AppComponent {
        return AppComponent(
            platformProviders = PlatformProviders()
        )
    }
}