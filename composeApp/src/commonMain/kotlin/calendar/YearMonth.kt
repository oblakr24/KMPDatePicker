package calendar

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class YearMonth(
    val month: Month,
    val year: Int,
) {

    fun atDay(day: Int): LocalDate = LocalDate(year, month, day)
    fun plusYears(years: Int) = copy(year = year + years)
    fun plusMonths(months: Int): YearMonth {
        val totalMonths = month.ordinal + 1 + months
        val newYear = year + (totalMonths - 1) / 12
        val newMonth = (totalMonths - 1) % 12
        return YearMonth(Month.entries[newMonth], newYear)
    }

    companion object {
        fun now(): YearMonth {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return YearMonth(
                month = now.month,
                year = now.year,
            )
        }
    }
}