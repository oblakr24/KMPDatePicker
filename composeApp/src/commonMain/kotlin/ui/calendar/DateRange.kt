package ui.calendar

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate

@Stable
@Immutable
data class DateRange(
    val from: LocalDate,
    val to: LocalDate,
)

data class AllowedRange(val minStart: LocalDate, val maxEnd: LocalDate, val maxStart: LocalDate?)
