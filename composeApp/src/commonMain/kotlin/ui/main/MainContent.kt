package ui.main

import PathInput
import alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import commonui.PrimaryTextButton
import commonui.rememberGenericDialogState
import kotlinx.coroutines.launch
import rememberGenericBottomSheetState
import ui.calendar.CalendarConfig
import ui.calendar.CalendarViewModel
import ui.calendar.CustomCalendarBottomSheet
import ui.calendar.CustomCalendarDialog
import ui.calendar.CustomCalendarDialogBuiltIn
import ui.calendar.CustomCalendarPagerDialog

data class MainContentUIState(
    val pathInput: PathInput,
    val drawer: MainDrawerUIState = MainDrawerUIState(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(state: MainContentUIState, onAction: (MainAction) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                LandingDrawer(state.drawer) { action ->
                    scope.launch {
                        drawerState.close()
                        onAction(action)
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "KMP Date Picker Demo",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = "Open",
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.alpha(0.5f)
                    )
                )
            }, content = { padding ->
                Column(
                    Modifier.fillMaxWidth().fillMaxHeight().padding(padding)
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    var confirmed: Boolean by remember { mutableStateOf(false) }
                    var singleSelection: Boolean by remember { mutableStateOf(false) }

                    val bottomSheetState = rememberGenericBottomSheetState()
                    val dialogState =
                        rememberGenericDialogState(isOpenInitially = state.pathInput == PathInput.OpenPickerDialog)
                    val builtInDialogState = rememberGenericDialogState()
                    val pagerDialogState = rememberGenericDialogState()

                    val calendarVMScope = rememberCoroutineScope()
                    val calendarVM = remember(singleSelection) {
                        CalendarViewModel(
                            calendarVMScope, CalendarConfig(
                                singleSelection = singleSelection
                            )
                        )
                    }

                    val selection = calendarVM.selection.collectAsState().value

                    CustomCalendarBottomSheet(bottomSheetState, calendarVM, footer = {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PrimaryTextButton("Confirm1") {
                                bottomSheetState.close()
                                confirmed = true
                            }
                        }
                    })

                    CustomCalendarDialog(dialogState, calendarVM, footer = {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PrimaryTextButton("Confirm") {
                                dialogState.close()
                                confirmed = true
                            }
                        }
                    })

                    CustomCalendarDialogBuiltIn(builtInDialogState, footer = {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PrimaryTextButton("Confirm") {
                                builtInDialogState.close()
                                confirmed = true
                            }
                        }
                    })

                    CustomCalendarPagerDialog(pagerDialogState, calendarVM, footer = {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PrimaryTextButton("Confirm") {
                                pagerDialogState.close()
                                confirmed = true
                            }
                        }
                    })

                    PrimaryTextButton("Open Date Picker") {
                        bottomSheetState.open()
                        confirmed = false
                    }

                    PrimaryTextButton("Open Date Dialog") {
                        dialogState.open()
                        confirmed = false
                    }

                    PrimaryTextButton("Open Date Dialog (builtin)") {
                        builtInDialogState.open()
                        confirmed = false
                    }

                    PrimaryTextButton("Open Date Pager Dialog") {
                        pagerDialogState.open()
                        confirmed = false
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Single selection",
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Switch(checked = singleSelection, onCheckedChange = { checked ->
                            singleSelection = checked
                        })
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Current selection:", color = MaterialTheme.colorScheme.onBackground)
                    if (confirmed) {
                        Text(
                            "$selection",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Text("Selection pending", color = MaterialTheme.colorScheme.onBackground)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

            }, bottomBar = {

            }
        )
    }
}