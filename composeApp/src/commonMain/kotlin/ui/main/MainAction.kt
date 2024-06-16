package ui.main

import data.DarkModeState

sealed interface MainAction {
    data class SetDarkMode(val new: DarkModeState) : MainAction
    data object OpenRepoUrl : MainAction
}
