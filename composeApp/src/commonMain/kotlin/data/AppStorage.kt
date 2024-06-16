package data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

// Not persisted - pending issue with multiplatform settings
class AppStorage {

    private val darkModeState: DarkModeState = DarkModeState.FOLLOW_SYSTEM

    private val _darkModeStateFlow: MutableStateFlow<DarkModeState> by lazy {
        MutableStateFlow(darkModeState)
    }

    val darkModeStateFlow = _darkModeStateFlow.asStateFlow()

    suspend fun updateDarkmode(new: DarkModeState) = withContext(Dispatchers.Default) {
        _darkModeStateFlow.update { new }
    }
}

enum class DarkModeState {
    ON, OFF, FOLLOW_SYSTEM
}