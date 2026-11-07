package com.urdrive.clientapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * PHASE 2 COMPONENT: VISUAL ELEMENTS (Screenshot 6)
 * Ratings, status pills, and tag indicators.
 * ============================================================================
 */

@Composable
fun UrDriveRatingStars(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating
            Text(
                text = if (isFilled) "★" else "☆",
                color = if (isFilled) UrDriveColors.Warning else UrDriveColors.Monochromatic06,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = rating.toString(),
            style = UrDriveTypography.ParagraphSmallMedium,
            color = UrDriveColors.FontDark
        )
    }
}

@Composable
fun UrDriveStatusPill(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    textColor: Color = UrDriveColors.FontWhite
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            style = UrDriveTypography.ParagraphSmallMedium.copy(fontSize = 11.sp),
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun UrDriveTagIndicator(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = UrDriveColors.Monochromatic03
    ) {
        Text(
            text = text,
            style = UrDriveTypography.ParagraphSmallRegular.copy(fontSize = 10.sp),
            color = UrDriveColors.FontWhite,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
