package com.urdrive.clientapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.presentation.components.UrDriveBottomNavigationBar
import com.urdrive.clientapp.presentation.components.UrDriveImageCard
import com.urdrive.clientapp.presentation.components.UrDriveSectionHeaderRow
import com.urdrive.clientapp.presentation.theme.UrDriveColors
import com.urdrive.clientapp.presentation.theme.UrDriveTypography

/**
 * ============================================================================
 * HOME SCREEN UI (Based on Figma template & Phase 1-3 design components)
 * Features: Top Navbar, Image Banner Slider, Section Headers, Horizontal Card Scroll,
 * Vertical Content Rows, and Bottom Navigation Footer with Light/Dark theme support.
 * ============================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean = false,
    onNavigate: (String) -> Unit = {}
) {
    var selectedBottomNavIndex by remember { mutableStateOf(0) }
    val backgroundColor = if (isDarkTheme) UrDriveColors.Monochromatic01 else UrDriveColors.Monochromatic09
    val surfaceColor = if (isDarkTheme) UrDriveColors.Monochromatic02 else UrDriveColors.Monochromatic10
    val textColor = if (isDarkTheme) UrDriveColors.FontWhite else UrDriveColors.FontDark
    val subTextColor = if (isDarkTheme) UrDriveColors.Monochromatic07 else UrDriveColors.Monochromatic05

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ur Drive Garage",
                            style = UrDriveTypography.Heading2,
                            color = textColor
                        )
                        Text(
                            text = "Welcome back, Driver",
                            style = UrDriveTypography.ParagraphSmallRegular,
                            color = subTextColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search action */ }) {
                        Text(text = "🔍", fontSize = 18.sp)
                    }
                    IconButton(onClick = { /* Menu action */ }) {
                        Text(text = "⋯", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        },
        bottomBar = {
            UrDriveBottomNavigationBar(
                selectedIndex = selectedBottomNavIndex,
                onItemSelected = { selectedBottomNavIndex = it },
                isDarkTheme = isDarkTheme
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. Image Banner Slider ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(surfaceColor),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🚗", fontSize = 64.sp)
                }
                Surface(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = UrDriveColors.Monochromatic01.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "1/24",
                        style = UrDriveTypography.ParagraphSmallMedium,
                        color = UrDriveColors.FontWhite,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // --- 2. Section Header: Featured Services ---
            UrDriveSectionHeaderRow(
                title = "Featured Services",
                isDarkTheme = isDarkTheme,
                showViewAll = true,
                onViewAllClick = { /* View all action */ }
            )

            // --- 3. Horizontal Scroll Section with Cards ---
            val cardItems = listOf(
                Triple("Engine Tune-Up", "Full diagnostic and spark plug replacement.", 4.8f),
                Triple("Brake Inspection", "Pad replacement and fluid check.", 4.9f),
                Triple("AC Maintenance", "Freon recharge and filter clean.", 4.7f)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cardItems) { item ->
                    UrDriveImageCard(
                        title = item.first,
                        description = item.second,
                        modifier = Modifier.width(200.dp),
                        isDarkTheme = isDarkTheme,
                        rating = item.third,
                        tag = "Popular",
                        onClick = { /* Card click action */ }
                    )
                }
            }

            // --- 4. Section Header: Recent Bookings ---
            UrDriveSectionHeaderRow(
                title = "Recent Bookings",
                isDarkTheme = isDarkTheme,
                showViewAll = true,
                onViewAllClick = { /* View all action */ }
            )

            // --- 5. Vertical List Section ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Toyota Fielder Maintenance", style = UrDriveTypography.Heading4, color = textColor)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "Scheduled for tomorrow at 10:00 AM", style = UrDriveTypography.ParagraphSmallRegular, color = subTextColor)
                        }
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = UrDriveColors.Success.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Confirmed",
                                style = UrDriveTypography.ParagraphSmallMedium,
                                color = UrDriveColors.Success,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
