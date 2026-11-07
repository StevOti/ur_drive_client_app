package com.urdrive.clientapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * PHASE 3 COMPONENT: BUTTONS (Screenshots 8, 9, 10)
 * Primary filled buttons, outlined buttons, icon buttons, and loading spinner states.
 * ============================================================================
 */

@Composable
fun UrDriveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    hasPlusIcon: Boolean = false,
    hasArrowIcon: Boolean = false,
    isOutlined: Boolean = false
) {
    val containerColor = if (isOutlined) Color.Transparent else UrDriveColors.Brand04
    val contentColor = if (isOutlined) UrDriveColors.Brand04 else UrDriveColors.FontWhite
    val borderColor = if (isOutlined) UrDriveColors.Brand04 else Color.Transparent

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(26.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = UrDriveColors.Monochromatic08,
            disabledContentColor = UrDriveColors.Monochromatic05
        ),
        border = if (isOutlined) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                if (hasPlusIcon) {
                    Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = text,
                    style = UrDriveTypography.ParagraphLargeMedium,
                    color = contentColor
                )
                if (hasArrowIcon) {
                    Text(text = "→", fontSize = 18.sp)
                }
            }
        }
    }
}
