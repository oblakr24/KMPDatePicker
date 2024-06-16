package ui.main

import PathInput
import com.rokoblak.kmpdatepicker.config.AppBuildConfig
import data.AppStorage
import data.DarkModeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainComponent(
    private val pathInput: PathInput,
    private val storage: AppStorage,
) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    val darkMode: StateFlow<DarkModeState> = storage.darkModeStateFlow

    val uiState = storage.darkModeStateFlow.map { darkMode ->
        val drawer = MainDrawerUIState(
            darkMode = darkMode,
            versionLabel = AppBuildConfig.VERSION,
        )
        MainContentUIState(
            drawer = drawer,
            pathInput = pathInput,
        )
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), MainContentUIState(pathInput = pathInput))

    fun onActon(action: MainAction) {
        when (action) {
            MainAction.FAQClicked -> {

            }
            MainAction.OpenRepoUrl -> {

            }
            is MainAction.SetDarkMode -> {
                scope.launch {
                    storage.updateDarkmode(action.new)
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}