package calendar

import DatePickerColors
import LocalDatePickerColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


object CalendarDefaults {

    @Composable
    fun calendarColors() = LocalDatePickerColors.current.defaultCalendarColors

    private val DatePickerColors.defaultCalendarColors: CalendarCellColors
        get() {
            return defaultCalendarColorsCached ?: CalendarCellColors(
                selectionRangeColor = selectionRangeColor,
                selectionMainColor = selectionMainColor,
                divider = divider,
                textColorInRange = textColorInRange,
                textColorInMainRange = textColorInMainRange,
                textColorOutOfRange = textColorOutOfRange,
                noSelectionBg = noSelectionBg,
                disabledText = disabledText,
            ).also {
                defaultCalendarColorsCached = it
            }
        }
}

@Immutable
data class CalendarCellColors(
    val selectionRangeColor: Color,
    val selectionMainColor: Color,
    val divider: Color,
    val textColorInMainRange: Color,
    val textColorInRange: Color,
    val textColorOutOfRange: Color,
    val noSelectionBg: Color,
    val disabledText: Color,
)
