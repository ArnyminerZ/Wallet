package com.arnyminerz.wallet.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arnyminerz.wallet.R

val outfit = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.outfit_black, weight = FontWeight.Black),
    androidx.compose.ui.text.font.Font(R.font.outfit_extra_bold, weight = FontWeight.ExtraBold),
    androidx.compose.ui.text.font.Font(R.font.outfit_bold, weight = FontWeight.Bold),
    androidx.compose.ui.text.font.Font(R.font.outfit_semi_bold, weight = FontWeight.SemiBold),
    androidx.compose.ui.text.font.Font(R.font.outfit_medium, weight = FontWeight.Medium),
    androidx.compose.ui.text.font.Font(R.font.outfit_regular, weight = FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.outfit_thin, weight = FontWeight.Thin),
    androidx.compose.ui.text.font.Font(R.font.outfit_light, weight = FontWeight.Light),
    androidx.compose.ui.text.font.Font(R.font.outfit_extra_light, weight = FontWeight.ExtraLight),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(fontFamily = outfit),
    labelMedium = TextStyle(fontFamily = outfit),
    labelSmall = TextStyle(fontFamily = outfit),
    titleLarge = TextStyle(fontFamily = outfit),
    titleMedium = TextStyle(fontFamily = outfit),
    titleSmall = TextStyle(fontFamily = outfit),
    headlineLarge = TextStyle(fontFamily = outfit),
    headlineMedium = TextStyle(fontFamily = outfit),
    headlineSmall = TextStyle(fontFamily = outfit),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)