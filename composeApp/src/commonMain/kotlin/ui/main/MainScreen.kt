package ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun MainScreen(component: MainComponent) {
    val state = component.uiState.collectAsState().value
    MainContent(state, onAction = component::onActon)
}