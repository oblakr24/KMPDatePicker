package calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.HorizontalSplit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import commonui.GenericDialog
import commonui.GenericDialogState
import commonui.PrimaryTextButton

@Composable
fun CustomCalendarPagerDialog(
    dialogState: GenericDialogState,
    vm: CalendarViewModel = rememberCalendarViewModel(),
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
) {
    GenericDialog(
        dialogState = dialogState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(onClick = {
                        dialogState.close()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = "Close",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        header?.invoke()
                    }
                    PrimaryTextButton(text = "Clear") {
                        vm.clearSelection()
                    }
                }
                CustomCalendarHorizontalDisplay(
                    vm = vm, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    footer?.let {
                        Surface(
                            shadowElevation = 12.dp,
                        ) {
                            it()
                        }
                    }
                }
            }
        }
    )
}
