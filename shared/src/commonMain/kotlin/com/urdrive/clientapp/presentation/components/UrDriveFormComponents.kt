package com.urdrive.clientapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * PHASE 3 COMPONENT: FORM CONTROLS (Screenshots 1, 2)
 * Toggles, base input fields, search bars, text inputs, password fields, and selectors.
 * ============================================================================
 */

@Composable
fun UrDriveToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = UrDriveColors.FontWhite,
            checkedTrackColor = UrDriveColors.Success,
            uncheckedThumbColor = UrDriveColors.Monochromatic06,
            uncheckedTrackColor = UrDriveColors.Monochromatic08
        )
    )
}

@Composable
fun UrDriveInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "Placeholder",
    helperText: String? = null,
    isError: Boolean = false,
    isPassword: Boolean = false,
    isDarkTheme: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic09
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark
    val borderColor = if (isError) UrDriveColors.Error else UrDriveColors.Brand05

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = UrDriveTypography.ParagraphSmallMedium,
                color = textColor
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = UrDriveColors.Monochromatic06) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            isError = isError,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                errorContainerColor = backgroundColor,
                focusedIndicatorColor = borderColor,
                unfocusedIndicatorColor = if (isError) UrDriveColors.Error else Color.Transparent,
                errorIndicatorColor = UrDriveColors.Error
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(text = if (passwordVisible) "👁️" else "🔒", fontSize = 16.sp)
                    }
                }
            } else null
        )

        if (helperText != null) {
            Text(
                text = helperText,
                style = UrDriveTypography.ParagraphSmallRegular.copy(fontSize = 11.sp),
                color = if (isError) UrDriveColors.Error else UrDriveColors.Monochromatic05
            )
        }
    }
}
