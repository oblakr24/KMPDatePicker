package ui.calendar

import AppColors
import AppTheme
import AppTypography
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WeekDaysRow(startDayOfWeek: DayOfWeek, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        for (i in 0 until 7) {
            val dayOfWeek = startDayOfWeek.plusDays(i)
            WeekDate(text = dayOfWeek.name.take(2), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun WeekDate(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.height(24.dp).padding(top = 2.dp),
        textAlign = TextAlign.Center,
        style = AppTypography.Body4SemiBold,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Preview
@Composable
private fun WeekDatesRowPreview() {
    AppTheme {
        WeekDaysRow(startDayOfWeek = DayOfWeek.MONDAY)
    }
}