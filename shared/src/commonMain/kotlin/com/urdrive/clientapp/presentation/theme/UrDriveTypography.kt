package com.urdrive.clientapp.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * ============================================================================
 * PHASE 1 DESIGN SYSTEM: TYPOGRAPHY (Gothic A1)
 * Extracted directly from Figma Typography specifications.
 * Reference: Heading 1-6 and Paragraph Large/Small variants.
 * ============================================================================
 */
object UrDriveTypography {
    // Heading 1: 26px font size, Bold weight, 32px line height -> Page title
    val Heading1 = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    )

    // Heading 2: 22px font size, Bold weight, 28px line height -> Paragraph header
    val Heading2 = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp
    )

    // Heading 3: 20px font size, Bold weight, 24px line height -> Paragraph header
    val Heading3 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )

    // Heading 4: 19px font size, Bold weight, 24px line height -> Row header, Card header
    val Heading4 = TextStyle(
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )

    // Heading 5: 18px font size, Bold weight, 24px line height, All Caps -> Section header, Small card header
    val Heading5 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )

    // Heading 6: 16px font size, Bold weight, 24px line height
    val Heading6 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )

    // Paragraph Large (Regular): 16px font size, Regular weight, 24px line height
    val ParagraphLargeRegular = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )

    // Paragraph Large (Medium): 16px font size, Medium weight, 24px line height
    val ParagraphLargeMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    )

    // Paragraph Small (Regular): 14px font size, Regular weight, 20px line height
    val ParagraphSmallRegular = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    )

    // Paragraph Small (Medium): 14px font size, Medium weight, 20px line height
    val ParagraphSmallMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    )
}
