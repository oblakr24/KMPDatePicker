package calendar

import kotlinx.datetime.LocalDate

interface IRange {
    val start: LocalDate
    val end: LocalDate
}

sealed interface RangeSelection {
    data object None : RangeSelection

    data class Range(
        override val start: LocalDate, override val end: LocalDate,
        val endInclusive: Boolean
    ) : RangeSelection, IRange
}