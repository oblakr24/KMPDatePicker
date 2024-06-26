package commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun rememberGenericDialogState(
    isOpenInitially: Boolean = false,
): GenericDialogState {
    val isOpen =
        remember { mutableStateOf(isOpenInitially) }

    return remember {
        GenericDialogState(
            isOpen = isOpen,
            close = {
                isOpen.value = false
            }
        )
    }
}

data class GenericDialogState(
    val isOpen: MutableState<Boolean>,
    val close: () -> Unit
) {
    fun open() {
        isOpen.value = true
    }
}

@Composable
fun GenericDialog(
    dialogState: GenericDialogState,
    contentColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable () -> Unit,
) {
    if (dialogState.isOpen.value) {
        Dialog(onDismissRequest = {
            dialogState.close()
        }, properties = DialogProperties(), content = {
            Box(modifier = Modifier.background(contentColor)) {
                content()
            }
        })
    }
}