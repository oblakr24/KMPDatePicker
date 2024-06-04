import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import calendar.CalendarConfig
import calendar.CalendarViewModel
import calendar.CustomCalendarBottomSheet
import calendar.CustomCalendarDialog
import commonui.PrimaryTextButton
import commonui.rememberGenericDialogState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    AppTheme {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("This is a demo")

            var confirmed: Boolean by remember { mutableStateOf(false) }
            var singleSelection: Boolean by remember { mutableStateOf(true) }

            val calendarState = rememberGenericBottomSheetState()
            val calendarDialogState = rememberGenericDialogState()

            val scope = rememberCoroutineScope()
            val calendarVM = remember(singleSelection) {
                CalendarViewModel(
                    scope, CalendarConfig(
                        singleSelection = singleSelection
                    )
                )
            }

            val selection = calendarVM.selection.collectAsState().value

            CustomCalendarBottomSheet(calendarState, calendarVM, header = {
            }, footer = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PrimaryTextButton("Confirm") {
                        calendarState.close()
                        confirmed = true
                    }
                }
            })

            CustomCalendarDialog(calendarDialogState, calendarVM, header = {
            }, footer = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PrimaryTextButton("Confirm") {
                        calendarDialogState.close()
                        confirmed = true
                    }
                }
            })

            PrimaryTextButton("Open Date Picker") {
                calendarState.open()
                confirmed = false
            }

            PrimaryTextButton("Open Date Dialog") {
                calendarDialogState.open()
                confirmed = false
            }

            Switch(checked = singleSelection, onCheckedChange = { checked ->
                singleSelection = checked
            })

            if (confirmed) {
                Text("Selection is: $selection")
            } else {
                Text("Selection pending")
            }
        }
    }
}