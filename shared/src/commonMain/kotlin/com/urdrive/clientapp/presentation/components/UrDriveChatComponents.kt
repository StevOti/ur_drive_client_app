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
 * PHASE 2 COMPONENT: CHAT (Screenshot 4)
 * Chat bubbles, chat stream layout, and message input bar.
 * ============================================================================
 */

@Composable
fun UrDriveChatBubble(
    message: String,
    timestamp: String,
    isOutgoing: Boolean,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false
) {
    val bubbleColor = if (isOutgoing) {
        UrDriveColors.Brand04
    } else {
        if (isDarkTheme) UrDriveColors.Monochromatic03 else UrDriveColors.Monochromatic09
    }
    val textColor = if (isOutgoing) UrDriveColors.FontWhite else (if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isOutgoing) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = bubbleColor,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message,
                    style = UrDriveTypography.ParagraphSmallRegular,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timestamp,
                    style = UrDriveTypography.ParagraphSmallRegular.copy(fontSize = 10.sp),
                    color = textColor.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun UrDriveChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    onSendClick: () -> Unit = {}
) {
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "➕", fontSize = 20.sp)

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Message...", color = UrDriveColors.Monochromatic05) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = UrDriveColors.Monochromatic09.copy(alpha = 0.5f),
                    unfocusedContainerColor = UrDriveColors.Monochromatic09.copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Text(text = "📷", fontSize = 20.sp)
            Text(text = "🎙️", fontSize = 20.sp)
        }
    }
}
