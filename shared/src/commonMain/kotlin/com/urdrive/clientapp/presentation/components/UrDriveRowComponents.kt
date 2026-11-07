package com.urdrive.clientapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * PHASE 2 COMPONENT: ROWS (Screenshots 8, 9, 10)
 * Counter rows, review rows, section headers, and content rows with actions & toggles.
 * ============================================================================
 */

@Composable
fun UrDriveSectionHeaderRow(
    title: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    showViewAll: Boolean = true,
    onViewAllClick: () -> Unit = {}
) {
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.uppercase(),
            style = UrDriveTypography.Heading5,
            color = textColor
        )

        if (showViewAll) {
            TextButton(onClick = onViewAllClick) {
                Text(
                    text = "View all",
                    style = UrDriveTypography.ParagraphSmallMedium,
                    color = UrDriveColors.Brand04
                )
            }
        }
    }
}

@Composable
fun UrDriveContentRow(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    leadingIcon: String = "📁",
    hasArrow: Boolean = true,
    hasToggle: Boolean = false,
    toggleState: Boolean = false,
    onToggleChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark
    val subTextColor = if (isDarkTheme) UrDriveColors.Monochromatic07 else UrDriveColors.Monochromatic05

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(UrDriveColors.Monochromatic09),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = leadingIcon, fontSize = 18.sp)
                }

                Column {
                    Text(text = title, style = UrDriveTypography.Heading4, color = textColor)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = subtitle, style = UrDriveTypography.ParagraphSmallRegular, color = subTextColor)
                }
            }

            when {
                hasToggle -> {
                    Switch(checked = toggleState, onCheckedChange = onToggleChanged)
                }
                hasArrow -> {
                    Text(text = "→", fontSize = 18.sp, color = subTextColor)
                }
            }
        }
    }
}
