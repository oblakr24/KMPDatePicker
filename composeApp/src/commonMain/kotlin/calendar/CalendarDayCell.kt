package calendar

import AppColors
import AppTheme
import AppTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

data class MonthData(
    val monthDisplay: String,
    val month: YearMonth,
    val index: Int,
    val items: List<DayItem>,
)

data class DayItem(
    val id: String,
    val text: String,
    val date: LocalDate,
    val monthType: MonthType,
    val styling: RangeStyling,
    val textStyling: TextStyling,
    val inRangeFromPrevMonth: Boolean = false,
    val inRangeFromNextMonth: Boolean = false,
) {
    enum class MonthType {
        PREV, CURR, NEXT,
    }

    enum class TextStyling {
        ENABLED, DISABLED, TODAY_DISABLED,
    }

    enum class RangeStyling {
        SINGLE,
        IN_RANGE,
        IN_RANGE_START,
        IN_RANGE_END,
        NORMAL,
    }
}

fun DayItem.RangeStyling.resolved() = when (this) {
    DayItem.RangeStyling.SINGLE -> ResolvedRangeStyling.SINGLE
    DayItem.RangeStyling.IN_RANGE -> ResolvedRangeStyling.IN_RANGE
    DayItem.RangeStyling.IN_RANGE_START -> ResolvedRangeStyling.IN_RANGE_START
    DayItem.RangeStyling.IN_RANGE_END -> ResolvedRangeStyling.IN_RANGE_END
    DayItem.RangeStyling.NORMAL -> ResolvedRangeStyling.NORMAL
}

enum class ResolvedRangeStyling(
    val hasInnerShape: Boolean = false,
    val bgFillLeft: Boolean = true,
    val bgFillRight: Boolean = true,
) {
    SINGLE(
        hasInnerShape = true,
        bgFillLeft = false,
        bgFillRight = false,
    ),
    IN_RANGE,
    IN_RANGE_START(
        hasInnerShape = true,
        bgFillLeft = false,
    ),
    IN_RANGE_END(
        hasInnerShape = true,
        bgFillRight = false,
    ),
    NORMAL;
}

@Composable
private fun ResolvedRangeStyling.bgColor(colors: CalendarCellColors) = when(this) {
    ResolvedRangeStyling.SINGLE -> colors.selectionMainColor
    ResolvedRangeStyling.IN_RANGE -> colors.selectionRangeColor
    ResolvedRangeStyling.IN_RANGE_START -> colors.selectionRangeColor
    ResolvedRangeStyling.IN_RANGE_END -> colors.selectionRangeColor
    ResolvedRangeStyling.NORMAL -> colors.noSelectionBg
}

@Composable
private fun ResolvedRangeStyling.textColor(colors: CalendarCellColors) = when(this) {
    ResolvedRangeStyling.SINGLE -> colors.textColorInMainRange
    ResolvedRangeStyling.IN_RANGE -> colors.textColorInRange
    ResolvedRangeStyling.IN_RANGE_START -> colors.textColorInMainRange
    ResolvedRangeStyling.IN_RANGE_END -> colors.textColorInMainRange
    ResolvedRangeStyling.NORMAL -> colors.textColorOutOfRange
}

@Composable
fun CalendarDayCell(item: DayItem,
                    colors: CalendarCellColors = CalendarDefaults.calendarColors(),
                    modifier: Modifier = Modifier,
                    onClick: () -> Unit) {
    val styling = item.styling.resolved()
    val height = 50.dp
    Box(
        modifier = modifier
            .height(height)
            .let {
                if (item.textStyling == DayItem.TextStyling.DISABLED) {
                    it
                } else {
                    it.clickable {
                        onClick()
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (styling.bgFillLeft && styling.bgFillRight) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(
                        when (item.monthType) {
                            DayItem.MonthType.PREV -> Color.Gray
                            DayItem.MonthType.CURR -> styling.bgColor(colors)
                            DayItem.MonthType.NEXT -> Color.Gray
                        }
                    ),
            )
        } else if (styling.bgFillLeft) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(end = 25.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        when (item.monthType) {
                            DayItem.MonthType.PREV -> Color.Gray
                            DayItem.MonthType.CURR -> styling.bgColor(colors)
                            DayItem.MonthType.NEXT -> Color.Gray
                        }
                    ),
            )
        } else if (styling.bgFillRight) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(start = 25.dp)
                    .align(Alignment.CenterEnd)
                    .background(
                        when (item.monthType) {
                            DayItem.MonthType.PREV -> Color.Gray
                            DayItem.MonthType.CURR -> styling.bgColor(colors)
                            DayItem.MonthType.NEXT -> Color.Gray
                        }
                    ),
            )
        }
        if (item.inRangeFromPrevMonth || item.inRangeFromNextMonth) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(Color.Gray),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)

                .let {
                    if (styling.hasInnerShape) {
                        it.background(colors.selectionMainColor, RoundedCornerShape(50))
                    } else if (item.textStyling == DayItem.TextStyling.TODAY_DISABLED) {
                        it.border(1.dp, colors.divider, RoundedCornerShape(50))
                    } else {
                        it
                    }
                },
        )
        if (item.monthType == DayItem.MonthType.CURR) {
            Text(
                text = item.text,
                textAlign = TextAlign.Center,
                color = when (item.textStyling) {
                    DayItem.TextStyling.ENABLED -> styling.textColor(colors)
                    DayItem.TextStyling.DISABLED -> colors.disabledText
                    DayItem.TextStyling.TODAY_DISABLED -> colors.disabledText
                },
                modifier = Modifier,
                style = AppTypography.Body4SemiBold,
            )
        }
    }
}

@Preview
@Composable
private fun CalendarDayCellPreview() {
    AppTheme {
        Row(modifier = Modifier.background(AppColors.BlackAlpha20)) {
            val item = DayItem(
                id = "1",
                text = "15",
                date = LocalDate.now(),
                monthType = DayItem.MonthType.CURR,
                styling = DayItem.RangeStyling.IN_RANGE_END,
                textStyling = DayItem.TextStyling.ENABLED,
                inRangeFromNextMonth = false,
                inRangeFromPrevMonth = false,
            )
            CalendarDayCell(item = item, modifier = Modifier.width(120.dp), onClick = {})
        }
    }
}

@Preview
@Composable
private fun CalendarDayCellInRangePreview() {
    AppTheme {
        Row(modifier = Modifier.background(AppColors.BlackAlpha20)) {
            val item = DayItem(
                id = "1",
                text = "15",
                date = LocalDate.now(),
                monthType = DayItem.MonthType.CURR,
                styling = DayItem.RangeStyling.IN_RANGE,
                textStyling = DayItem.TextStyling.ENABLED,
                inRangeFromNextMonth = false,
                inRangeFromPrevMonth = false
            )
            CalendarDayCell(item = item, modifier = Modifier.width(120.dp), onClick = {})
        }
    }
}

@Preview
@Composable
private fun CalendarDayCellTodayDisabledPreview() {
    AppTheme {
        Row(modifier = Modifier.background(AppColors.BlackAlpha20)) {
            val item = DayItem(
                id = "1",
                text = "15",
                date = LocalDate.now(),
                monthType = DayItem.MonthType.CURR,
                styling = DayItem.RangeStyling.NORMAL,
                textStyling = DayItem.TextStyling.TODAY_DISABLED,
                inRangeFromNextMonth = false,
                inRangeFromPrevMonth = false
            )
            CalendarDayCell(item = item, modifier = Modifier.width(120.dp), onClick = {})
        }
    }
}

@Preview
@Composable
private fun CalendarDayCellPastDisabledInRangePreview() {
    AppTheme {
        Row(modifier = Modifier.background(AppColors.BlackAlpha20)) {
            val item = DayItem(
                id = "1",
                text = "15",
                date = LocalDate.now(),
                monthType = DayItem.MonthType.CURR,
                styling = DayItem.RangeStyling.NORMAL,
                textStyling = DayItem.TextStyling.ENABLED,
                inRangeFromNextMonth = false,
                inRangeFromPrevMonth = true,
            )
            CalendarDayCell(item = item, modifier = Modifier.width(120.dp), onClick = {})
        }
    }
}
