package com.urdrive.clientapp.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi

import ur_drive_client_app.shared.generated.resources.Res

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val resourcePath: String,
    val fallbackEmoji: String
)

/**
 * Onboarding Intro Screen featuring automatic sliding pager, Lottie header animations,
 * sharp-cornered buttons, bottom-anchored layout, and auth navigation.
 */
@Composable
fun AuthIntroScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Streamline Your Life",
            subtitle = "Find balance with our powerful app",
            resourcePath = "drawable/intro_screen_one.json",
            fallbackEmoji = "📸"
        ),
        OnboardingPage(
            title = "Boost Productivity",
            subtitle = "Simplify tasks, boost productivity",
            resourcePath = "drawable/intro_screen_two.json",
            fallbackEmoji = "⚡"
        ),
        OnboardingPage(
            title = "Stay Organized",
            subtitle = "Reclaim control over your daily routine",
            resourcePath = "drawable/intro_screen_three.json",
            fallbackEmoji = "🌿"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    // Robust automatic sliding effect using an infinite coroutine loop
    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000L)
            val nextPage = (pagerState.currentPage + 1) % pages.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 600)
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom // Bottom-anchored layout
        ) {
            // Pager for Onboarding Pages
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Lottie Animation Container
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieIntroAnimationView(
                            resourcePath = page.resourcePath,
                            fallbackEmoji = page.fallbackEmoji
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = page.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = page.subtitle,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Page Indicator Dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.Gray.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Sign Up Button (Sharp corners)
            Button(
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Sign up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login / Log in Button (Sharp corners)
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RectangleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Log in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Renders Lottie animation from Compose Resources with fallback emoji.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LottieIntroAnimationView(resourcePath: String, fallbackEmoji: String) {
    var jsonString by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    LaunchedEffect(resourcePath) {
        try {
            val bytes = Res.readBytes(resourcePath)
            jsonString = bytes.decodeToString()
        } catch (_: Exception) {
            hasError = true
        }
    }

    when {
        hasError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = fallbackEmoji, fontSize = 64.sp)
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
                modifier = Modifier.size(200.dp)
            )
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp))
            }
        }
    }
}
