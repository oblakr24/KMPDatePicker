package ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import commonui.GenericDialog
import commonui.GenericDialogState
import commonui.PrimaryTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCalendarDialogBuiltIn(
    dialogState: GenericDialogState,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
) {
    GenericDialog(
        dialogState = dialogState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
            ) {
                val datePickerState = rememberDateRangePickerState(
                    yearRange = 2024..2026
                )
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
                        datePickerState.setSelection(null, null)
                    }
                }
                DateRangePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp, horizontal = 12.dp),
                    state = datePickerState,
                    showModeToggle = false,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 4.dp)
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
