package com.urdrive.clientapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * PHASE 2 COMPONENT: CARDS (Screenshots 1, 2, 3)
 * Reusable card designs including Image Cards, Profile Cards, and Card Headers.
 * Styled with UrDrive Design System (Colors, Typography, Spacing).
 * ============================================================================
 */

@Composable
fun UrDriveImageCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    rating: Float? = null,
    tag: String? = null,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark
    val subTextColor = if (isDarkTheme) UrDriveColors.Monochromatic07 else UrDriveColors.Monochromatic05

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Placeholder Image Container with optional Tag indicator (e.g. "1/24")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(UrDriveColors.Monochromatic08),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (tag != null) {
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = UrDriveColors.Monochromatic01.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = tag,
                            style = UrDriveTypography.ParagraphSmallRegular,
                            color = UrDriveColors.FontWhite,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Card Header & Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = UrDriveTypography.Heading4,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = UrDriveTypography.ParagraphSmallRegular,
                        color = subTextColor
                    )
                }

                if (rating != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "★", color = UrDriveColors.Warning, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = rating.toString(),
                            style = UrDriveTypography.ParagraphSmallMedium,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UrDriveProfileCard(
    fullName: String,
    userRole: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark
    val subTextColor = if (isDarkTheme) UrDriveColors.Monochromatic07 else UrDriveColors.Monochromatic05

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(UrDriveColors.Brand08),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👤", fontSize = 28.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = fullName,
                    style = UrDriveTypography.Heading3,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = userRole,
                    style = UrDriveTypography.ParagraphSmallRegular,
                    color = subTextColor
                )
            }
        }
    }
}
