package com.urdrive.clientapp.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * OTP / 6-Digit Code Verification Screen:
 * - Content centered vertically while remaining fully responsive (scrollable when keyboard opens).
 * - Code input fields strictly accept integer digits only (no decimals or symbols) with auto-advance focus.
 */
@Composable
fun AuthOtpScreen(
    viewModel: AuthViewModel,
    isSignUpFlow: Boolean,
    onBackClick: () -> Unit,
    onRedirectToLogin: () -> Unit,
    onRedirectToDashboard: () -> Unit
) {
    val codeDigits = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    var secondsRemaining by remember { mutableStateOf(60) }
    var isExpired by remember { mutableStateOf(false) }

    val displayPhoneNumber = if (viewModel.pendingPhoneNumber.isNotBlank()) {
        viewModel.pendingPhoneNumber
    } else {
        "+254712345678"
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }
        isExpired = true
    }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
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
                    .verticalScroll(rememberScrollState()) // Responsive scrolling when soft keyboard opens
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center // Centered vertically as requested
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

                Spacer(modifier = Modifier.height(12.dp))

                // Lottie Header Animation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    LottieHeaderView(
                        primaryPath = "drawable/verification_code_otp_v2.json",
                        altPath = "drawable/verification code-otp v2.json",
                        fallbackEmoji = "🔒"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title "6 digit-code"
                Text(
                    text = "6 digit-code",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Enter the code we sent to $displayPhoneNumber",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 6 Digit Code Input Boxes (Strictly integer digits only, auto-advance focus)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // First 3 digits
                    for (i in 0..2) {
                        OtpDigitBox(
                            value = codeDigits[i],
                            focusRequester = focusRequesters[i],
                            onValueChange = { newVal ->
                                val digitsOnly = newVal.filter { it.isDigit() }
                                if (digitsOnly.isNotEmpty()) {
                                    codeDigits[i] = digitsOnly.take(1)
                                    if (i < 5) {
                                        focusRequesters[i + 1].requestFocus()
                                    }
                                } else {
                                    codeDigits[i] = ""
                                }
                            }
                        )
                    }

                    // Hyphen separator
                    Text(
                        text = "-",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )

                    // Last 3 digits
                    for (i in 3..5) {
                        OtpDigitBox(
                            value = codeDigits[i],
                            focusRequester = focusRequesters[i],
                            onValueChange = { newVal ->
                                val digitsOnly = newVal.filter { it.isDigit() }
                                if (digitsOnly.isNotEmpty()) {
                                    codeDigits[i] = digitsOnly.take(1)
                                    if (i < 5) {
                                        focusRequesters[i + 1].requestFocus()
                                    }
                                } else {
                                    codeDigits[i] = ""
                                }
                            }
                        )
                    }
                }

                if (uiState.generalError != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.generalError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Expiry timer
                val minutes = secondsRemaining / 60
                val secs = secondsRemaining % 60
                val timeFormatted = "$minutes:${if (secs < 10) "0" else ""}${secs}s"

                Text(
                    text = if (!isExpired) "Code is expired in $timeFormatted" else "Code has expired",
                    fontSize = 14.sp,
                    color = if (!isExpired) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Resend Button
                TextButton(
                    onClick = {
                        secondsRemaining = 60
                        isExpired = false
                    }
                ) {
                    Text(
                        text = "Resend",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Verify Button
                Button(
                    onClick = {
                        val fullCode = codeDigits.joinToString("")
                        viewModel.verifyOtpAndCompleteAuth(
                            otpCode = fullCode,
                            isSignUpFlow = isSignUpFlow,
                            onRedirectToLogin = onRedirectToLogin,
                            onRedirectToDashboard = onRedirectToDashboard
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
                        Text("Verify", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OtpDigitBox(
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .size(42.dp, 50.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(contentAlignment = Alignment.Center) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                shape = RectangleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
