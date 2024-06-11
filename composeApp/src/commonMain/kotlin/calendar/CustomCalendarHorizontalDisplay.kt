package calendar

import AppColors
import AppTheme
import AppTypography
import alpha
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCalendarHorizontalDisplay(
    config: () -> CalendarConfig = { CalendarConfig() },
    vm: CalendarViewModel = rememberCalendarViewModel(config = config),
    daysHeader: @Composable RowScope.(CalendarConfig) -> Unit = { cfg ->
        WeekDaysRow(
            startDayOfWeek = cfg.weekStartDay,
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.primaryContainer)
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

    val pageCount = vm.monthCount.collectAsState()
    val pagerState = rememberPagerState(pageCount = {
        pageCount.value
    })
    val scope = rememberCoroutineScope()
    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalPager(state = pagerState, modifier = modifier.weight(1f)) { pageIdx ->

            val rowItems = vm.createItemsForMonth(index = pageIdx).collectAsState().value
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState, modifier = Modifier.padding(horizontal = 8.dp)) {
                stickyHeader {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        val leftEnabled = pagerState.currentPage > 0
                        IconButton(onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }, modifier = Modifier.wrapContentWidth(), enabled = leftEnabled) {
                            Icon(
                                imageVector = Icons.Filled.ArrowCircleLeft,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.alpha(if (leftEnabled) 1f else 0.5f),
                                contentDescription = "Left",
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        val config = remember(config)
                        daysHeader(config)
                        val rightEnabled = pagerState.currentPage < pagerState.pageCount - 1
                        IconButton(onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }, modifier = Modifier.wrapContentWidth(), enabled = rightEnabled) {
                            Icon(
                                imageVector = Icons.Filled.ArrowCircleRight,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.alpha(if (rightEnabled) 1f else 0.5f),
                                contentDescription = "Right",
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }

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

                            CalendarRow.Spacer -> {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                        }
                    })
            }
        }
    }
}

@Preview
@Composable
fun CustomHorizontalCalendarDisplayPreview() {
    AppTheme {
        val vm = rememberCalendarViewModel()
        CustomCalendarHorizontalDisplay(
            vm = vm, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        )
    }
}

@Preview
@Composable
fun CustomHorizontalCalendarDisplaySelectionPreview() {
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
        CustomCalendarHorizontalDisplay(
            vm = vm, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        )
    }
}

@Preview
@Composable
fun CustomHorizontalCalendarDisplayOpenSelectionPreview() {
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
        CustomCalendarHorizontalDisplay(
            vm = vm, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        )
    }
}
