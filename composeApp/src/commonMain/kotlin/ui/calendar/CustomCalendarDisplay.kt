package ui.calendar

import AppColors
import AppTheme
import AppTypography
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed class CalendarRow(open val id: String, open val type: String, open val index: Int) {
    data class Dates(
        val items: List<DayItem>, override val id: String,
        override val index: Int
    ) :
        CalendarRow(id, type = "Row", index = index)

    data class MonthHeader(val title: String, override val id: String, override val index: Int) :
        CalendarRow(id, type = "MonthHeader", index = index)

    data object Spacer: CalendarRow("Spacer", type = "Spacer", index = -1)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCalendarDisplay(
    config: () -> CalendarConfig = { CalendarConfig() },
    vm: CalendarViewModel = rememberCalendarViewModel(config = config),
    daysHeader: @Composable (CalendarConfig) -> Unit = { cfg ->
        WeekDaysRow(
            startDayOfWeek = cfg.weekStartDay,
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
        )
    },
    dayCell: @Composable RowScope.(DayItem) -> Unit = { dayItem ->
        CalendarDayCell(
            item = dayItem,
            modifier = Modifier.weight(1f),
            onClick = {
                vm.onClick(dayItem.date)
            })
    },
    monthHeader: @Composable (CalendarRow.MonthHeader) -> Unit = { header ->
        Text(
            text = header.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            style = AppTypography.Body4,
        )
    },
    modifier: Modifier = Modifier
) {
    val rowItems = vm.rowItems.collectAsState().value

    val state = rememberLazyListState()

    LaunchedEffect(key1 = vm, block = {
        vm.scrollToPos.collectLatest { index ->
            state.animateScrollToItem(index)
        }
    })

    LazyColumn(state = state, modifier = modifier) {
        stickyHeader {
            val config = remember(config)
            daysHeader(config)
        }

        items(
            count = rowItems.size,
            key = { rowItems[it].id },
            contentType = { rowItems[it].type },
            itemContent = { rowIdx ->
                when (val item = rowItems[rowIdx]) {
                    is CalendarRow.MonthHeader -> {
                        monthHeader(item)
                    }

                    is CalendarRow.Dates -> {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val dayItems = item.items
                            for (dayItem in dayItems) {
                                if (dayItem.monthType == DayItem.MonthType.CURR) {
                                    dayCell(dayItem)
                                } else {
                                    Box(
                                        Modifier
                                            .weight(1f)
                                            .let {
                                                if (dayItem.inRangeFromPrevMonth || dayItem.inRangeFromNextMonth) {
                                                    it.background(AppColors.BackgroundLightGreen)
                                                } else {
                                                    it
                                                }
                                            }) {
                                    }
                                }
                            }
                        }
                    }

                    CalendarRow.Spacer -> Unit
                }
            })
    }
}

@Preview
@Composable
fun CustomCalendarDisplayPreview() {
    AppTheme {
        val vm = rememberCalendarViewModel()
        CustomCalendarDisplay(
            vm = vm, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        )
    }
}

@Preview
@Composable
fun CustomCalendarDisplaySelectionPreview() {
    AppTheme {
        val vm = rememberCalendarViewModel(
            config = {
                CalendarConfig(
                    startMonth = YearMonth.now(),
                    endMonth = YearMonth.now().plusMonths(4),
                    initialSelection = RangeSelection.Range(
                        LocalDate.now().plusDays(3),
                        LocalDate.now().plusDays(14),
                        endInclusive = true
                    ),
                )
            }
        )
        CustomCalendarDisplay(
            vm = vm, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        )
    }
}

@Preview
@Composable
fun CustomCalendarDisplayOpenSelectionPreview() {
    AppTheme {
        val vm = rememberCalendarViewModel(
            config = {
                CalendarConfig(
                    startMonth = YearMonth.now(),
                    endMonth = YearMonth.now().plusMonths(4),
                    initialSelection = RangeSelection.Range(
                        LocalDate.now().minusDays(35),
                        LocalDate.now().plusDays(2),
                        endInclusive = false,
                    ),
                )
            }
        )
        CustomCalendarDisplay(
            vm = vm, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        )
    }
}
