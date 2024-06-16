package commonui

import AppTheme
import alpha
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryTextButton(
    text: String,
    small: Boolean = false,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(6.dp),
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = ButtonDefaults.MinWidth,
            minHeight = 10.dp,
        ),
        enabled = enabled,
        contentPadding = contentPadding,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

            Text(
                text = text,
                style = if (small) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.alpha(if (enabled) 1.0f else 0.6f),
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = icon,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.alpha(if (enabled) 1.0f else 0.6f),
                    contentDescription = "Close",
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryTextButtonPreview() {
    AppTheme {
        PrimaryTextButton(text = "Text", onClick = {})
    }
}

@Preview
@Composable
private fun PrimaryTextButtonShortPreview() {
    AppTheme {
        PrimaryTextButton(
            text = "Short",
            small = true,
            contentPadding = PaddingValues(2.dp),
            onClick = {})
    }
}

@Preview
@Composable
private fun PrimaryTextButtonDisabledPreview() {
    AppTheme {
        PrimaryTextButton(text = "Text", enabled = false, onClick = {})
    }
}