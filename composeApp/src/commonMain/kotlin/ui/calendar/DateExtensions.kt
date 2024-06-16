package ui.calendar

import ui.calendar.YearMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun LocalDate.plusDays(days: Int) = plus(DatePeriod(days = days))
fun LocalDate.minusDays(days: Int) = minus(DatePeriod(days = days))
fun LocalDate.plusMonths(months: Int) = plus(DatePeriod(months = months))
fun LocalDate.withDayOfMonth(dayOfMonth: Int) = LocalDate(year, month, dayOfMonth)

fun LocalDate.Companion.now(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun YearMonth.atEndOfMonth(): LocalDate {
    val next = if (month == Month.DECEMBER) {
        LocalDate(year + 1, Month.JANUARY, dayOfMonth = 1)
    } else {
        LocalDate(year, Month.entries[month.ordinal + 1], dayOfMonth = 1)
    }
    val dayBefore = next.minus(DatePeriod(days = 1))
    return dayBefore
}

fun DayOfWeek.plusDays(days: Int): DayOfWeek {
    val newDayIndex = (this.ordinal + days % 7 + 7) % 7
    return DayOfWeek.entries[newDayIndex]
}

fun LocalDate.isAfter(other: LocalDate) = this > other
fun LocalDate.isBefore(other: LocalDate) = this < other

fun LocalDate.previousOrSame(dayOfWeek: DayOfWeek): LocalDate {
    var daysBack = dayOfWeek.ordinal - this.dayOfWeek.ordinal
    if (daysBack > 0) {
        daysBack -= 7
    }
    return this.plusDays(daysBack)
}

fun LocalDate.nextOrSame(dayOfWeek: DayOfWeek): LocalDate {
    var days = (dayOfWeek.ordinal - this.dayOfWeek.ordinal) % 7
    if (days < 0) {
        days += 7
    }
    return this.plusDays(days)
}

fun YearMonth.isAfter(other: YearMonth): Boolean {
    if (year > other.year) return true
    if (year < other.year) return false
    return month > other.month
}
fun YearMonth.isBefore(other: YearMonth): Boolean {
    if (this == other) return false
    return !isAfter(other)
}

fun LocalDate.isEqual(other: LocalDate) = this == other