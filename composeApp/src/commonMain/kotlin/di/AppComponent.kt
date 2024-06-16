package di

import data.AppStorage

class AppComponent {

    private var storage: AppStorage? = null

    fun storage(): AppStorage {
        return storage ?: AppStorage().also {
            storage = it
        }
    }
}


fun createAppComponent(): AppComponent = AppComponent()