import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import calendar.CalendarCellColors

class DatePickerColors(
    val selectionRangeColor: Color,
    val selectionMainColor: Color,
    val divider: Color,
    val textColorInRange: Color,
    val textColorInMainRange: Color,
    val textColorOutOfRange: Color,
    val noSelectionBg: Color,
    val disabledText: Color,
    val isDark: Boolean,
) {
    internal var defaultCalendarColorsCached: CalendarCellColors? = null
}

fun darkDatePickerColors(
): DatePickerColors = DatePickerColors(
    selectionMainColor = Color.Gray,
    selectionRangeColor = Color.Black,
    divider = AppColors.GrayLine,
    textColorInRange = AppColors.PrimaryGreen,
    textColorInMainRange = AppColors.WhiteBG,
    textColorOutOfRange = AppColors.WhiteBG,
    noSelectionBg = Color.Transparent,
    disabledText = AppColors.DisabledGray,
    isDark = true,
)

fun lightDatePickerColors(): DatePickerColors = DatePickerColors(
    selectionMainColor = AppColors.PrimaryGreen,
    selectionRangeColor = AppColors.BackgroundLightGreen,
    divider = AppColors.GrayLine,
    textColorInRange = AppColors.PrimaryGreen,
    textColorInMainRange = AppColors.WhiteBG,
    textColorOutOfRange = AppColors.PrimaryDarkNavy,
    noSelectionBg = Color.Transparent,
    disabledText = AppColors.DisabledGray,
    isDark = false,
)

val LocalDatePickerColors = staticCompositionLocalOf { lightDatePickerColors() }
