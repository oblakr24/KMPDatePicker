package di

import data.AppStorage
import data.PlatformProviders

class AppComponent(
    private val platformProviders: PlatformProviders
) {

    private var storage: AppStorage? = null

    fun storage(): AppStorage {
        return storage ?: AppStorage().also {
            storage = it
        }
    }

    fun intentHandler() = platformProviders.intentHandler()
}

expect class ComponentCreator {

    fun createAppComponent(): AppComponent

}