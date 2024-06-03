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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
        IN_RANGE,
        IN_RANGE_START,
        IN_RANGE_END,
        NORMAL,
    }
}

fun DayItem.RangeStyling.resolved() = when (this) {
    DayItem.RangeStyling.IN_RANGE -> ResolvedRangeStyling.IN_RANGE
    DayItem.RangeStyling.IN_RANGE_START -> ResolvedRangeStyling.IN_RANGE_START
    DayItem.RangeStyling.IN_RANGE_END -> ResolvedRangeStyling.IN_RANGE_END
    DayItem.RangeStyling.NORMAL -> ResolvedRangeStyling.NORMAL
}

enum class ResolvedRangeStyling(
    val bgColor: Color,
    val textColor: Color,
    val shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    val hasInnerShape: Boolean = false,
    val innerShapeColor: Color = AppColors.PrimaryGreen,
    val bgFillLeft: Boolean = true,
    val bgFillRight: Boolean = true,
) {
    IN_RANGE(bgColor = AppColors.BackgroundLightGreen, textColor = AppColors.PrimaryDarkNavy),
    IN_RANGE_START(
        bgColor = AppColors.BackgroundLightGreen,
        hasInnerShape = true,
        textColor = Color.White,
        shape = RoundedCornerShape(
            50, 0, 0, 50
        ),
        bgFillLeft = false,
    ),
    IN_RANGE_END(
        bgColor = AppColors.BackgroundLightGreen,
        hasInnerShape = true,
        textColor = Color.White,
        shape = RoundedCornerShape(
            0, 50, 50, 0
        ),
        bgFillRight = false,
    ),
    IN_RANGE_END_DISABLED(
        bgColor = AppColors.BackgroundLightGreen,
        hasInnerShape = true,
        textColor = AppColors.WhiteBG,
        shape = RoundedCornerShape(
            0, 50, 50, 0
        ),
        innerShapeColor = AppColors.DisabledGray,
        bgFillRight = false,
    ),
    IN_RANGE_END_DISABLED_INSIDE(
        bgColor = AppColors.BackgroundLightGreen,
        hasInnerShape = true,
        textColor = AppColors.WhiteBG,
        innerShapeColor = AppColors.DisabledGray,
    ),
    NORMAL(bgColor = Color.Transparent, textColor = AppColors.PrimaryDarkNavy);
}

@Composable
fun CalendarDayCell(item: DayItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val styling = item.styling.resolved()
    Box(
        modifier = modifier
            .height(50.dp)
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
                    .fillMaxSize()
                    .background(
                        when (item.monthType) {
                            DayItem.MonthType.PREV -> Color.Gray
                            DayItem.MonthType.CURR -> styling.bgColor
                            DayItem.MonthType.NEXT -> Color.Gray
                        }
                    ),
            )
        } else if (styling.bgFillLeft) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterStart)
                    .background(
                        when (item.monthType) {
                            DayItem.MonthType.PREV -> Color.Gray
                            DayItem.MonthType.CURR -> styling.bgColor
                            DayItem.MonthType.NEXT -> Color.Gray
                        }
                    ),
            )
        } else if (styling.bgFillRight) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterEnd)
                    .background(
                        when (item.monthType) {
                            DayItem.MonthType.PREV -> Color.Gray
                            DayItem.MonthType.CURR -> styling.bgColor
                            DayItem.MonthType.NEXT -> Color.Gray
                        }
                    ),
            )
        }
        if (item.inRangeFromPrevMonth || item.inRangeFromNextMonth) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
            )
        }
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
                .aspectRatio(1f)

                .let {
                    if (styling.hasInnerShape) {
                        it.background(styling.innerShapeColor, RoundedCornerShape(50))
                    } else if (item.textStyling == DayItem.TextStyling.TODAY_DISABLED) {
                        it.border(1.dp, AppColors.GrayLine, RoundedCornerShape(50))
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
                    DayItem.TextStyling.ENABLED -> styling.textColor
                    DayItem.TextStyling.DISABLED -> AppColors.GrayLine
                    DayItem.TextStyling.TODAY_DISABLED -> AppColors.GrayLine
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
