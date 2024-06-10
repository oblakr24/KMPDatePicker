package calendar

import AppColors
import GenericBottomSheetState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import commonui.PrimaryTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCalendarBottomSheet(
    bottomSheetState: GenericBottomSheetState,
    vm: CalendarViewModel = rememberCalendarViewModel(),
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
) {
    if (bottomSheetState.isOpen.value) {
        val insets = BottomSheetDefaults.windowInsets
        ModalBottomSheet(
            contentColor = MaterialTheme.colorScheme.primaryContainer,
            scrimColor = AppColors.BlackAlpha20,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            windowInsets = insets.union(WindowInsets(top = 0.dp, bottom = 48.dp)),
            onDismissRequest = {
                bottomSheetState.close()
            },
            modifier = Modifier,
            sheetState = bottomSheetState.sheetState,
            tonalElevation = 12.dp,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
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
                            bottomSheetState.close()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                tint =  MaterialTheme.colorScheme.onPrimaryContainer,
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
                    CustomCalendarDisplay(
                        vm = vm, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .weight(1f)
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
}
