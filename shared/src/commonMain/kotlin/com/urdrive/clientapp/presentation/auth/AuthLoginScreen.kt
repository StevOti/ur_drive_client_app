package com.urdrive.clientapp.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Sign In / Login Screen: Users sign in using phone number (with country code dropdown) and password.
 * Center-aligned layout, inline error messages appearing below fields.
 */
@Composable
fun AuthLoginScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onNavigateToOtp: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val countries = remember {
        listOf(
            Country("Kenya", "+254", "🇰🇪"),
            Country("United States", "+1", "🇺🇸"),
            Country("United Kingdom", "+44", "🇬🇧"),
            Country("Tanzania", "+255", "🇹🇿"),
            Country("Uganda", "+256", "🇺🇬"),
            Country("Nigeria", "+234", "🇳🇬"),
            Country("South Africa", "+27", "🇿🇦"),
            Country("Ghana", "+233", "🇬🇭"),
            Country("Rwanda", "+250", "🇷🇼"),
            Country("Ethiopia", "+251", "🇪🇹"),
            Country("India", "+91", "🇮🇳"),
            Country("Canada", "+1", "🇨🇦"),
            Country("Australia", "+61", "🇦🇺"),
            Country("Germany", "+49", "🇩🇪"),
            Country("United Arab Emirates", "+971", "🇦🇪")
        )
    }
    var selectedCountry by remember { mutableStateOf(countries[0]) } // Kenya (+254)
    var countryDropdownExpanded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.clearErrors()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500)) { it / 4 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center // Center-aligned vertically as requested
            ) {
                // Top Back Arrow strictly at top left corner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.offset(x = (-12).dp)
                    ) {
                        Text("←", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Lottie Header Animation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    LottieHeaderView(
                        primaryPath = "drawable/female_avatar.json",
                        altPath = "drawable/female avatar.json",
                        fallbackEmoji = "👋"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title "Welcome!"
                Text(
                    text = "Welcome!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Text(
                    text = "Hi, Enter your details to sign in to your account",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Phone Number Row (Country Selector + Phone Input)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Surface(
                            modifier = Modifier
                                .width(110.dp)
                                .height(56.dp)
                                .clickable { countryDropdownExpanded = true },
                            shape = RectangleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${selectedCountry.flag} ${selectedCountry.code}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text("▼", fontSize = 10.sp, color = Color.Gray)
                            }
                        }

                        DropdownMenu(
                            expanded = countryDropdownExpanded,
                            onDismissRequest = { countryDropdownExpanded = false },
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            countries.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text("${country.flag} ${country.name} (${country.code})") },
                                    onClick = {
                                        selectedCountry = country
                                        countryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    TextField(
                        value = phoneNumber,
                        onValueChange = { if (it.length <= 9) phoneNumber = it },
                        label = { Text("712345678 (9 digits)") },
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = false
                    )
                }
                if (uiState.phoneError != null) {
                    Text(
                        text = uiState.phoneError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    isError = false
                )
                if (uiState.passwordError != null) {
                    Text(
                        text = uiState.passwordError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (uiState.generalError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = uiState.generalError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Primary Login Button
                Button(
                    onClick = {
                        viewModel.validateAndPrepareLogin(
                            countryCode = selectedCountry.code,
                            phone = phoneNumber,
                            pass = password,
                            onNavigateToOtp = onNavigateToOtp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RectangleShape,
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Or Sign In via Section
                Text(
                    text = "Or Sign In via",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Social Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        LoginSocialIconOnlyButton(iconType = SocialIconType.GOOGLE)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        LoginSocialIconOnlyButton(iconType = SocialIconType.APPLE)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        LoginSocialIconOnlyButton(iconType = SocialIconType.FACEBOOK)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Switch to Sign Up Link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Sign up",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onSignUpClick() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LoginSocialIconOnlyButton(iconType: SocialIconType) {
    OutlinedButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (iconType) {
                SocialIconType.GOOGLE -> Text(text = "G", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                SocialIconType.APPLE -> AppleVectorIcon()
                SocialIconType.FACEBOOK -> Text(text = "f", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1877F2))
            }
        }
    }
}
