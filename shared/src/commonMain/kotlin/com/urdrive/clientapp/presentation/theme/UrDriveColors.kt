package com.urdrive.clientapp.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * ============================================================================
 * PHASE 1 DESIGN SYSTEM: COLORS
 * Extracted directly from Figma Colour palette specifications.
 * Reference: Monochromatic, Brand, Neutral, Success, Warning, Error, and Font states.
 * ============================================================================
 */
object UrDriveColors {
    // --- Monochromatic Scale ---
    val Monochromatic01 = Color(0xFF1A1A1A)
    val Monochromatic02 = Color(0xFF333333)
    val Monochromatic03 = Color(0xFF4D4D4D)
    val Monochromatic04 = Color(0xFF666666)
    val Monochromatic05 = Color(0xFF808080)
    val Monochromatic06 = Color(0xFF999999)
    val Monochromatic07 = Color(0xFFB2B2B2)
    val Monochromatic08 = Color(0xFFCCCCCC)
    val Monochromatic09 = Color(0xFFF2F2F2)
    val Monochromatic10 = Color(0xFFFFFFFF)

    // --- Brand Purple Scale ---
    val Brand01 = Color(0xFF1F0D26)
    val Brand02 = Color(0xFF3E1A40)
    val Brand03 = Color(0xFF5D2673)
    val Brand04 = Color(0xFF7C3399)
    val Brand05 = Color(0xFF9B40BF)
    val Brand06 = Color(0xFFAF66CC)
    val Brand07 = Color(0xFFC38CD9)
    val Brand08 = Color(0xFFD7B2E5)
    val Brand09 = Color(0xFFEBD9F2)
    val Brand10 = Color(0xFFF5ECF9)

    // --- Functional Accent Colors ---
    val Neutral = Color(0xFF4892D4)
    val Success = Color(0xFF4DCFC0)
    val Warning = Color(0xFFEEA63A)
    val Error = Color(0xFFE52B67)

    // --- Typography & Font Intent Colors ---
    val FontDark = Color(0xFF0F0F0F)
    val FontMid = Color(0xFF0F0F0F).copy(alpha = 0.6f)      // 60% opacity variant
    val FontLight = Color(0xFF0F0F0F).copy(alpha = 0.3f)     // 30% opacity variant
    val FontWhite = Color(0xFFFFFFFF)
}
