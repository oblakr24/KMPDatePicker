import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import calendar.CalendarConfig
import calendar.CalendarViewModel
import calendar.CustomCalendarBottomSheet
import calendar.CustomCalendarDialog
import calendar.CustomCalendarDialogBuiltIn
import calendar.CustomCalendarPagerDialog
import com.rokoblak.kmpdatepicker.config.AppBuildConfig
import commonui.PrimaryTextButton
import commonui.rememberGenericDialogState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(pathInput: PathInput = PathInput.None) {
    var darkTheme: Boolean by remember { mutableStateOf(false) }
    var useSystemTheme: Boolean by remember { mutableStateOf(false) }
    AppTheme(overrideDarkMode = if (useSystemTheme) null else darkTheme) {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.background).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Input: $pathInput", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text("Dark theme", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = darkTheme, onCheckedChange = { checked ->
                    darkTheme = checked
                })
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text("Use system theme", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = useSystemTheme, onCheckedChange = { checked ->
                    useSystemTheme = checked
                })
            }

            var confirmed: Boolean by remember { mutableStateOf(false) }
            var singleSelection: Boolean by remember { mutableStateOf(false) }

            val calendarState = rememberGenericBottomSheetState()
            val calendarDialogState = rememberGenericDialogState(isOpenInitially = false)
            val calendarBuiltInDialogState = rememberGenericDialogState()
            val calendarPagerDialogState = rememberGenericDialogState()

            val scope = rememberCoroutineScope()
            val calendarVM = remember(singleSelection) {
                CalendarViewModel(
                    scope, CalendarConfig(
                        singleSelection = singleSelection
                    )
                )
            }

            val selection = calendarVM.selection.collectAsState().value

            CustomCalendarBottomSheet(calendarState, calendarVM, footer = {
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

            CustomCalendarDialog(calendarDialogState, calendarVM, footer = {
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

            // Has an issue on WASM production
//            CustomCalendarDialogBuiltIn(calendarBuiltInDialogState, footer = {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    PrimaryTextButton("Confirm") {
//                        calendarBuiltInDialogState.close()
//                        confirmed = true
//                    }
//                }
//            })

            CustomCalendarPagerDialog(calendarPagerDialogState, calendarVM, footer = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PrimaryTextButton("Confirm") {
                        calendarPagerDialogState.close()
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

//            PrimaryTextButton("Open Date Dialog (builtin)") {
//                calendarBuiltInDialogState.open()
//                confirmed = false
//            }

            PrimaryTextButton("Open Date Pager Dialog") {
                calendarPagerDialogState.open()
                confirmed = false
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text("Single selection", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = singleSelection, onCheckedChange = { checked ->
                    singleSelection = checked
                })
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Current selection:", color = MaterialTheme.colorScheme.onBackground)
            if (confirmed) {
                Text("$selection", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onBackground)
            } else {
                Text("Selection pending", color = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text("Version: ${AppBuildConfig.VERSION}", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}