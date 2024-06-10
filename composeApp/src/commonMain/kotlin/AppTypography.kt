import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// TODO: Setup
private val SourceSansProFont: FontFamily? = null

// These are fallbacks for the most part, i.e. for styling of nested components without direct access.
val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = SourceSansProFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = TextUnit.Unspecified
    ),
    labelLarge = TextStyle(
        fontFamily = SourceSansProFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = TextUnit.Unspecified,
    ),
    labelMedium = TextStyle(
        fontFamily = SourceSansProFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = TextUnit.Unspecified,
    ),
    labelSmall = TextStyle(
        fontFamily = SourceSansProFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = TextUnit.Unspecified,
    ),
)

object AppTypography {
    val Body4 = TextStyle(
        fontFamily = SourceSansProFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 17.sp,
    )
    val Body4SemiBold = TextStyle(
        fontFamily = SourceSansProFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 17.sp,
    )
}
