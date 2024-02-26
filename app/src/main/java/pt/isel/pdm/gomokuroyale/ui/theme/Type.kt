package pt.isel.pdm.gomokuroyale.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R

val GomokuRoyale =
    FontFamily(
        Font(R.font.aloevera),
//        Font(R.font.gomoku_royale_bold, FontWeight.Bold),
//        Font(R.font.gomoku_royale_italic, FontWeight.Normal, FontStyle.Italic),
//        Font(R.font.gomoku_royale_bold_italic, FontWeight.Bold, FontStyle.Italic)
    )

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GomokuRoyale,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = GomokuRoyale,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = GomokuRoyale,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)