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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * PHASE 2 COMPONENT: FOOTER / BOTTOM NAVIGATION (Screenshot 5)
 * Bottom navigation bars, icon footers, and user profile footer cards.
 * ============================================================================
 */

@Composable
fun UrDriveBottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false
) {
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10
    val iconColor = if (isDarkTheme) UrDriveColors.Monochromatic07 else UrDriveColors.Monochromatic04
    val selectedColor = UrDriveColors.Brand04

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icons = listOf("🏠", "❤️", "🛒", "🔍", "👤")
            icons.forEachIndexed { index, icon ->
                val isSelected = index == selectedIndex
                IconButton(onClick = { onItemSelected(index) }) {
                    Text(
                        text = icon,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun UrDriveUserFooterCard(
    fullName: String,
    userRole: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    onCallClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark
    val subTextColor = if (isDarkTheme) UrDriveColors.Monochromatic07 else UrDriveColors.Monochromatic05

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(UrDriveColors.Brand08),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👤", fontSize = 20.sp)
                }

                Column {
                    Text(text = fullName, style = UrDriveTypography.Heading4, color = textColor)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = userRole, style = UrDriveTypography.ParagraphSmallRegular, color = subTextColor)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onCallClick) {
                    Text(text = "📞", fontSize = 18.sp)
                }
                IconButton(onClick = onChatClick) {
                    Text(text = "💬", fontSize = 18.sp)
                }
            }
        }
    }
}
