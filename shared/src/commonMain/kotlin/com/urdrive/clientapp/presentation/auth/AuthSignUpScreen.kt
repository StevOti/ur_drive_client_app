package com.urdrive.clientapp.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

import ur_drive_client_app.shared.generated.resources.Res

/**
 * Sign Up Screen featuring center-aligned layout, email validation, 9-digit phone number starting with 7,
 * secure password policy, inline field error messages, number-only keyboards, comprehensive country selector,
 * "Or sign up via" label, and social auth buttons.
 */
@Composable
fun AuthSignUpScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToOtp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

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
                // Top Bar with Back Arrow strictly at top left corner
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
                        fallbackEmoji = "👩"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title "Join us!"
                Text(
                    text = "Join us!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle
                Text(
                    text = "Hi, Create your account to get started with Ur Drive",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Email Field
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    isError = false
                )
                if (uiState.emailError != null) {
                    Text(
                        text = uiState.emailError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                    label = { Text("Password (>8 chars, number & symbol)") },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm password") },
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
                if (uiState.confirmPasswordError != null) {
                    Text(
                        text = uiState.confirmPasswordError!!,
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

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Sign Up Button
                Button(
                    onClick = {
                        viewModel.validateAndPrepareSignUp(
                            email = email,
                            countryCode = selectedCountry.code,
                            phone = phoneNumber,
                            pass = password,
                            confirmPass = confirmPassword,
                            onNavigateToOtp = onNavigateToOtp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Sign up", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // "Or sign up via" label as requested
                Text(
                    text = "Or sign up via",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Social Icon-Only Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        SignUpSocialIconOnlyButton(iconType = SocialIconType.GOOGLE)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        SignUpSocialIconOnlyButton(iconType = SocialIconType.APPLE)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        SignUpSocialIconOnlyButton(iconType = SocialIconType.FACEBOOK)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Switch to Login Link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already had an account? ",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Log in",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SignUpSocialIconOnlyButton(iconType: SocialIconType) {
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

@Composable
fun AppleVectorIcon(modifier: Modifier = Modifier) {
    val tint = LocalContentColor.current
    Canvas(modifier = modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.53f, size.height * 0.12f)
            cubicTo(size.width * 0.58f, size.height * 0.05f, size.width * 0.65f, size.height * 0.0f, size.width * 0.73f, size.height * 0.01f)
            cubicTo(size.width * 0.74f, size.height * 0.11f, size.width * 0.70f, size.height * 0.20f, size.width * 0.64f, size.height * 0.26f)
            cubicTo(size.width * 0.59f, size.height * 0.31f, size.width * 0.51f, size.height * 0.35f, size.width * 0.44f, size.height * 0.34f)
            cubicTo(size.width * 0.43f, size.height * 0.25f, size.width * 0.47f, size.height * 0.17f, size.width * 0.53f, size.height * 0.12f)
            close()
            moveTo(size.width * 0.79f, size.height * 0.41f)
            cubicTo(size.width * 0.73f, size.height * 0.37f, size.width * 0.67f, size.height * 0.37f, size.width * 0.62f, size.height * 0.40f)
            cubicTo(size.width * 0.57f, size.height * 0.43f, size.width * 0.53f, size.height * 0.43f, size.width * 0.49f, size.height * 0.40f)
            cubicTo(size.width * 0.43f, size.height * 0.37f, size.width * 0.37f, size.height * 0.38f, size.width * 0.32f, size.height * 0.43f)
            cubicTo(size.width * 0.21f, size.height * 0.54f, size.width * 0.23f, size.height * 0.73f, size.width * 0.34f, size.height * 0.88f)
            cubicTo(size.width * 0.40f, size.height * 0.96f, size.width * 0.45f, size.height * 1.0f, size.width * 0.52f, size.height * 1.0f)
            cubicTo(size.width * 0.58f, size.height * 1.0f, size.width * 0.61f, size.height * 0.96f, size.width * 0.69f, size.height * 0.96f)
            cubicTo(size.width * 0.76f, size.height * 0.96f, size.width * 0.80f, size.height * 1.0f, size.width * 0.86f, size.height * 0.98f)
            cubicTo(size.width * 0.93f, size.height * 0.95f, size.width * 0.97f, size.height * 0.88f, size.width * 1.01f, size.height * 0.81f)
            cubicTo(size.width * 0.88f, size.height * 0.74f, size.width * 0.87f, size.height * 0.56f, size.width * 0.95f, size.height * 0.47f)
            cubicTo(size.width * 0.90f, size.height * 0.43f, size.width * 0.84f, size.height * 0.42f, size.width * 0.79f, size.height * 0.41f)
            close()
        }
        drawPath(path = path, color = tint)
    }
}

/**
 * Renders Lottie header animation loaded from Compose Resources with automatic fallback.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LottieHeaderView(primaryPath: String, altPath: String, fallbackEmoji: String) {
    var jsonString by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    LaunchedEffect(primaryPath, altPath) {
        try {
            val bytes = Res.readBytes(primaryPath)
            jsonString = bytes.decodeToString()
        } catch (_: Exception) {
            try {
                val bytes = Res.readBytes(altPath)
                jsonString = bytes.decodeToString()
            } catch (_: Exception) {
                try {
                    val cleanPath = primaryPath.replace("drawable/", "files/")
                    val bytes = Res.readBytes(cleanPath)
                    jsonString = bytes.decodeToString()
                } catch (_: Exception) {
                    hasError = true
                }
            }
        }
    }

    when {
        hasError -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = fallbackEmoji, fontSize = 24.sp)
                }
            }
        }
        jsonString.isNotEmpty() -> {
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.JsonString(jsonString)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = Int.MAX_VALUE
            )

            val painter = rememberLottiePainter(
                composition = composition,
                progress = { progress }
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxHeight().width(120.dp)
            )
        }
        else -> {
            Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }
}
