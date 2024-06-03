package calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import calendar.RangeSelection.*
import calendar.DayItem.*
import format
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
fun rememberCalendarViewModel(
    config: CalendarConfig = CalendarConfig()
): CalendarViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        CalendarViewModel(scope, config)
    }
}

data class CalendarConfig(
    val startMonth: YearMonth = YearMonth.now(),
    val endMonth: YearMonth = startMonth.plusYears(1),
    val minStart: LocalDate = LocalDate.now().plusDays(1),
    val maxStart: LocalDate? = null,
    val initialSelection: RangeSelection = None,
    val initialScrollTo: LocalDate? = null,
    val limitMaxEnd: LocalDate? = null,
    val minimumSelectionRange: Int = 0,
    val weekStartDay: DayOfWeek = DayOfWeek.MONDAY,
)

class CalendarViewModel(
    scope: CoroutineScope,
    private val config: CalendarConfig,
) {

    private val months by lazy { createMonthsBetween(config.startMonth, config.endMonth) }

    private val monthData by lazy {
        months.map { MonthData(it.format(), items = createDayItemsInAMonth(it)) }
    }

    private val _selection = MutableStateFlow(config.initialSelection)
    private val _initialSelection = MutableStateFlow(config.initialSelection)

    val selection = _selection.asStateFlow()
    val initialSelection = _initialSelection.asStateFlow()

    private val limit = MutableStateFlow(
        AllowedRange(
            minStart = config.minStart,
            maxEnd = config.limitMaxEnd ?: config.endMonth.atEndOfMonth(),
            maxStart = config.maxStart,
        )
    )

    private val decoratedMonths: StateFlow<List<MonthData>> by lazy {
        combine(_selection, limit) { selection, limit ->
            createDecoratedMonthItems(selection, limit, monthData)
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(5000),
            createDecoratedMonthItems(_selection.value, limit.value, monthData)
        )
    }

    val rowItems: StateFlow<List<CalendarRow>> by lazy {
        decoratedMonths.map { months ->
            createCalendarRowItems(months)
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(5000),
            createCalendarRowItems(decoratedMonths.value)
        )
    }

    val scrollToPos: Flow<Int> = flow {
        delay(500)
        val initialScrollTo = config.initialScrollTo
        if (initialScrollTo != null) {
            val index =
                rowItems.value.filterIsInstance<CalendarRow.Dates>().firstNotNullOfOrNull { row ->
                    val foundItem = row.items.firstOrNull { items ->
                        items.date == initialScrollTo
                    }
                    row.takeIf { foundItem != null }
                }?.index
            if (index != null) {
                emit(index)
            }
        }
    }

    fun updateMaxEnd(new: LocalDate) {
        limit.update {
            it.copy(maxEnd = new)
        }
    }

    fun onClick(date: LocalDate) {
        _selection.update {
            it.modifyOnClick(date)
        }
    }

    private fun RangeSelection.modifyOnClick(date: LocalDate): RangeSelection {
        return when (this) {
            None -> Range(
                start = date,
                end = date.plusDays(config.minimumSelectionRange),
                endInclusive = false
            )

            is Range -> {
                if (endInclusive) {
                    if (end == date) None else None.modifyOnClick(date)
                } else {
                    if (date > start) {
                        if (date > end || date == end) {
                            copy(end = date, endInclusive = true)
                        } else {
                            this // We ignore the click, as we cannot modify the end date within the minimum days period
                        }
                    } else {
                        Range(
                            start = date,
                            end = date.plusDays(config.minimumSelectionRange.let { if (it > 1) it - 1 else 0 }),
                            endInclusive = false
                        )
                    }
                }
            }
        }
    }

    fun clearSelection() {
        _selection.update { None }
    }

    fun setInitialSelection(rangeSelection: DateRange?) {
        if (_selection.value == None) {
            if (rangeSelection != null) {
                _selection.value =
                    Range(
                        rangeSelection.from,
                        rangeSelection.to,
                        endInclusive = true
                    )
            } else {
                clearSelection()
            }
            _initialSelection.value = _selection.value
        } else {
            _initialSelection.value = if (rangeSelection != null) {
                Range(rangeSelection.from, rangeSelection.to, endInclusive = true)
            } else {
                None
            }
        }
    }

    private fun createDecoratedMonthItems(
        selection: RangeSelection,
        limit: AllowedRange,
        months: List<MonthData>
    ): List<MonthData> = months.map { monthData ->
        val today = LocalDate.now()
        val items = monthData.items.map { item ->
            item.decorate(selection, limit, today)
        }
        monthData.copy(items = items)
    }

    private fun createCalendarRowItems(months: List<MonthData>): List<CalendarRow> {
        val allRows = mutableListOf<CalendarRow>()
        val columns = 7
        var index = 0
        months.map { monthItem ->
            val items = monthItem.items
            val numberOfRows = (items.size + columns - 1) / columns

            allRows.add(
                CalendarRow.MonthHeader(
                    monthItem.monthDisplay,
                    id = monthItem.monthDisplay,
                    index = index,
                )
            )
            index++

            val monthRows = (0..numberOfRows).map { rowIndex ->
                val rowItems = mutableListOf<DayItem>()
                for (columnIndex in 0 until 7) {
                    val itemIndex = rowIndex * 7 + columnIndex
                    if (itemIndex < items.size) {
                        rowItems.add(items[itemIndex])
                    }
                }
                rowItems
            }

            allRows.addAll(monthRows.mapIndexed { idx, dayItems ->
                val row = CalendarRow.Dates(
                    dayItems,
                    id = "Row_${monthItem.monthDisplay}_$idx",
                    index = index,
                )
                index++
                row
            })
        }
        return allRows
    }

    private fun DayItem.decorate(
        selection: RangeSelection,
        allowedRange: AllowedRange,
        today: LocalDate,
    ): DayItem {
        val isDisabled = when (selection) {
            None -> date.isBefore(allowedRange.minStart)
                    || date.isAfter(allowedRange.maxEnd)
                    || (allowedRange.maxStart?.let { date.isAfter(it) } ?: false)

            is Range -> date.isBefore(allowedRange.minStart)
                    || date.isAfter(allowedRange.maxEnd)
        }

        val textStyling = if (date.isEqual(today)) {
            TextStyling.TODAY_DISABLED
        } else if (isDisabled) {
            TextStyling.DISABLED
        } else {
            TextStyling.ENABLED
        }

        val inRangeFromPrevMonth =
            if (monthType == MonthType.PREV && selection is IRange) {
                val selectionEnd = selection.end
                val startOfNextMonth = date.plusMonths(1).withDayOfMonth(1)
                selection.start.isBefore(startOfNextMonth) && (selectionEnd.isEqual(
                    startOfNextMonth
                ) || selectionEnd.isAfter(startOfNextMonth))
            } else {
                false
            }
        val inRangeFromNextMonth =
            if (monthType == MonthType.NEXT && selection is IRange) {
                val selectionEnd = selection.end
                val endOfLastMonth = date.withDayOfMonth(1).minusDays(1)
                selection.start.isBefore(endOfLastMonth) && selectionEnd.isAfter(endOfLastMonth)
            } else {
                false
            }

        return copy(
            styling = resolveRangeStyling(selection),
            textStyling = textStyling,
            inRangeFromPrevMonth = inRangeFromPrevMonth,
            inRangeFromNextMonth = inRangeFromNextMonth
        )
    }

    private fun DayItem.resolveRangeStyling(selection: RangeSelection) = when (selection) {
        is Range -> when {
            date.isEqual(selection.start) -> RangeStyling.IN_RANGE_START
            date.isEqual(selection.end) && selection.endInclusive -> RangeStyling.IN_RANGE_END
            (date.isAfter(selection.start) && (date.isBefore(selection.end) || (date.isEqual(
                selection.end
            )))) -> RangeStyling.IN_RANGE

            else -> RangeStyling.NORMAL
        }

        else -> RangeStyling.NORMAL
    }

    private fun createDayItemsInAMonth(yearMonth: YearMonth): List<DayItem> {
        val firstDayOfCurrentMonth = yearMonth.atDay(1)
        val lastDayOfCurrentMonth = yearMonth.atEndOfMonth()

        val startCalendar = firstDayOfCurrentMonth.previousOrSame(config.weekStartDay)
        val endCalendar = lastDayOfCurrentMonth.nextOrSame(config.weekStartDay.plusDays(6))

        val daysList = mutableListOf<DayItem>()
        var currentDate = startCalendar
        while (currentDate.isBefore(endCalendar) || currentDate.isEqual(endCalendar)) {
            val monthType = when {
                currentDate.isBefore(firstDayOfCurrentMonth) -> MonthType.PREV
                currentDate.isAfter(lastDayOfCurrentMonth) -> MonthType.NEXT
                else -> MonthType.CURR
            }

            daysList.add(
                DayItem(
                    id = currentDate.toString(),
                    text = currentDate.dayOfMonth.toString(),
                    date = currentDate,
                    monthType = monthType,
                    styling = RangeStyling.NORMAL,
                    textStyling = TextStyling.ENABLED,
                )
            )
            currentDate = currentDate.plusDays(1)
        }

        return daysList
    }

    private fun createMonthsBetween(startMonth: YearMonth, endMonth: YearMonth): List<YearMonth> {
        val monthsList = mutableListOf<YearMonth>()
        var currentMonth = startMonth
        while (currentMonth.isBefore(endMonth)) {
            monthsList.add(currentMonth)
            currentMonth = currentMonth.plusMonths(1)
        }
        return monthsList
    }
}
