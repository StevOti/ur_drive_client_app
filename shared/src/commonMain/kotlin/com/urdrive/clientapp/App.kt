package com.urdrive.clientapp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urdrive.clientapp.domain.model.AddOnPart
import com.urdrive.clientapp.domain.model.AddOnType
import com.urdrive.clientapp.domain.model.ServiceItem
import com.urdrive.clientapp.domain.model.Vehicle
import com.urdrive.clientapp.presentation.auth.*

/**
 * Main application composable for Ur Drive Vehicle Management & Service Booking Platform.
 * Features automatic light/dark theme adaptation, onboarding flows, and authentication (Sign In & Sign Up).
 */
@Composable
@Preview
fun App() {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF60A5FA),
            secondary = Color(0xFF2DD4BF),
            background = Color(0xFF0F172A),
            surface = Color(0xFF1E293B),
            primaryContainer = Color(0xFF1E3A8A),
            onBackground = Color(0xFFF8FAFC),
            onSurfaceVariant = Color(0xFF94A3B8)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF1E3A8A),
            secondary = Color(0xFF0D9488),
            background = Color(0xFFF8FAFC),
            surface = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFDBEAFE),
            onBackground = Color(0xFF0F172A),
            onSurfaceVariant = Color(0xFF64748B)
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        var currentScreen by remember { mutableStateOf(Screen.AUTH_INTRO) }
        val authViewModel = remember { AuthViewModel() }

        var selectedVehicle by remember {
            mutableStateOf(Vehicle(make = "Toyota", model = "Camry", year = 2023, mileage = 12500))
        }
        var selectedService by remember { mutableStateOf<ServiceItem?>(null) }
        val selectedAddOns = remember { mutableStateListOf<AddOnPart>() }

        val sampleServices = listOf(
            ServiceItem("1", "Full Synthetic Oil Change", "Includes filter replacement & fluid top-up", 79.99, 45, "Maintenance"),
            ServiceItem("2", "Brake Pad Replacement", "Front or rear ceramic brake pads + rotor inspection", 189.99, 90, "Repair"),
            ServiceItem("3", "Advanced Engine Diagnostics", "Comprehensive computer scan & sensor check", 119.99, 60, "Inspection")
        )

        val sampleAddOns = listOf(
            AddOnPart("a1", "Cabin Air Filter Replacement", "Fresh airflow & allergen reduction", 29.99, AddOnType.PART),
            AddOnPart("a2", "Coolant System Flush", "Prevents engine overheating and corrosion", 89.99, AddOnType.PART),
            AddOnPart("a3", "Multi-Point Safety Check-up", "Inspection of tires, suspension, and battery", 0.0, AddOnType.CHECKUP)
        )

        val calculatedTotal = (selectedService?.basePrice ?: 0.0) + selectedAddOns.sumOf { it.price }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize().safeContentPadding()) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 } togetherWith
                                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { -it / 2 }
                    }
                ) { screen ->
                    when (screen) {
                        Screen.AUTH_INTRO -> AuthIntroScreen(
                            onSignUpClick = { currentScreen = Screen.SIGN_UP },
                            onLoginClick = { currentScreen = Screen.LOGIN }
                        )
                        Screen.SIGN_UP -> AuthSignUpScreen(
                            viewModel = authViewModel,
                            onBackClick = { currentScreen = Screen.AUTH_INTRO },
                            onLoginClick = { currentScreen = Screen.LOGIN },
                            onNavigateToOtp = { currentScreen = Screen.OTP_SIGNUP }
                        )
                        Screen.OTP_SIGNUP -> AuthOtpScreen(
                            viewModel = authViewModel,
                            isSignUpFlow = true,
                            onBackClick = { currentScreen = Screen.SIGN_UP },
                            onRedirectToLogin = { currentScreen = Screen.LOGIN },
                            onRedirectToDashboard = { currentScreen = Screen.ONBOARDING_STEP_1 }
                        )
                        Screen.LOGIN -> AuthLoginScreen(
                            viewModel = authViewModel,
                            onBackClick = { currentScreen = Screen.AUTH_INTRO },
                            onSignUpClick = { currentScreen = Screen.SIGN_UP },
                            onNavigateToOtp = { currentScreen = Screen.OTP_LOGIN }
                        )
                        Screen.OTP_LOGIN -> AuthOtpScreen(
                            viewModel = authViewModel,
                            isSignUpFlow = false,
                            onBackClick = { currentScreen = Screen.LOGIN },
                            onRedirectToLogin = { currentScreen = Screen.LOGIN },
                            onRedirectToDashboard = { currentScreen = Screen.DASHBOARD }
                        )
                        // Post-OTP Sign Up Onboarding Wizard Steps
                        Screen.ONBOARDING_STEP_1 -> OnboardingStep1Screen(
                            viewModel = authViewModel,
                            onNext = { currentScreen = Screen.ONBOARDING_STEP_2 },
                            onBack = { currentScreen = Screen.OTP_SIGNUP }
                        )
                        Screen.ONBOARDING_STEP_2 -> OnboardingStep2Screen(
                            viewModel = authViewModel,
                            onNext = { currentScreen = Screen.ONBOARDING_STEP_3 },
                            onBack = { currentScreen = Screen.ONBOARDING_STEP_1 }
                        )
                        Screen.ONBOARDING_STEP_3 -> OnboardingStep3Screen(
                            viewModel = authViewModel,
                            onNext = { currentScreen = Screen.ONBOARDING_STEP_4 },
                            onBack = { currentScreen = Screen.ONBOARDING_STEP_2 }
                        )
                        Screen.ONBOARDING_STEP_4 -> OnboardingStep4Screen(
                            viewModel = authViewModel,
                            onNext = { currentScreen = Screen.TERMS_CONDITIONS },
                            onBack = { currentScreen = Screen.ONBOARDING_STEP_3 }
                        )
                        Screen.TERMS_CONDITIONS -> TermsConditionsScreen(
                            onAgreeAndContinue = {
                                currentScreen = Screen.LOGIN
                            },
                            onDecline = { currentScreen = Screen.ONBOARDING_STEP_4 }
                        )
                        Screen.DASHBOARD -> DashboardScreen(
                            vehicle = selectedVehicle,
                            viewModel = authViewModel,
                            onNavigateToBooking = { currentScreen = Screen.SERVICE_CATALOG },
                            onNavigateToGarage = { currentScreen = Screen.GARAGE },
                            onLogout = { currentScreen = Screen.AUTH_INTRO }
                        )
                        Screen.GARAGE -> GarageScreen(
                            vehicle = selectedVehicle,
                            onBack = { currentScreen = Screen.DASHBOARD }
                        )
                        Screen.SERVICE_CATALOG -> ServiceCatalogScreen(
                            services = sampleServices,
                            addOns = sampleAddOns,
                            selectedService = selectedService,
                            selectedAddOns = selectedAddOns,
                            totalPrice = calculatedTotal,
                            onSelectService = { selectedService = it },
                            onToggleAddOn = { addOn ->
                                if (selectedAddOns.contains(addOn)) selectedAddOns.remove(addOn)
                                else selectedAddOns.add(addOn)
                            },
                            onProceedToCheckout = { currentScreen = Screen.CHECKOUT },
                            onBack = { currentScreen = Screen.DASHBOARD }
                        )
                        Screen.CHECKOUT -> CheckoutScreen(
                            vehicle = selectedVehicle,
                            service = selectedService,
                            addOns = selectedAddOns,
                            totalPrice = calculatedTotal,
                            onConfirmBooking = { currentScreen = Screen.SUCCESS },
                            onBack = { currentScreen = Screen.SERVICE_CATALOG }
                        )
                        Screen.SUCCESS -> SuccessScreen(
                            onFinish = {
                                selectedService = null
                                selectedAddOns.clear()
                                currentScreen = Screen.DASHBOARD
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class Screen {
    AUTH_INTRO, SIGN_UP, LOGIN, OTP_SIGNUP, OTP_LOGIN,
    ONBOARDING_STEP_1, ONBOARDING_STEP_2, ONBOARDING_STEP_3, ONBOARDING_STEP_4, TERMS_CONDITIONS,
    DASHBOARD, GARAGE, SERVICE_CATALOG, CHECKOUT, SUCCESS
}

@Composable
fun DashboardScreen(
    vehicle: Vehicle,
    viewModel: AuthViewModel,
    onNavigateToBooking: () -> Unit,
    onNavigateToGarage: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Ur Drive", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Vehicle Management & Service Hub", fontSize = 14.sp, color = Color.Gray)
            }
            // Log Out Button
            OutlinedButton(
                onClick = {
                    viewModel.logout(onLogout)
                }
            ) {
                Text("Log Out", color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Vehicle Garage Card
        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Active Vehicle", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("${vehicle.year} ${vehicle.make} ${vehicle.model}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Odometer: ${vehicle.mileage} miles", fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onNavigateToGarage, modifier = Modifier.fillMaxWidth()) {
                    Text("Manage Garage")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Booking Action Card
        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Need maintenance or check-up?", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Book professional services and complement with custom parts.", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateToBooking, modifier = Modifier.fillMaxWidth()) {
                    Text("Book Service Now")
                }
            }
        }
    }
}

@Composable
fun GarageScreen(vehicle: Vehicle, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("My Garage", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("${vehicle.year} ${vehicle.make} ${vehicle.model}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("License Plate: ABC-1234")
                Text("Mileage: ${vehicle.mileage} miles")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Back to Dashboard") }
    }
}

@Composable
fun ServiceCatalogScreen(
    services: List<ServiceItem>,
    addOns: List<AddOnPart>,
    selectedService: ServiceItem?,
    selectedAddOns: List<AddOnPart>,
    totalPrice: Double,
    onSelectService: (ServiceItem) -> Unit,
    onToggleAddOn: (AddOnPart) -> Unit,
    onProceedToCheckout: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Select Service & Add-ons", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onBack) { Text("Back") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Text("Primary Service", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(services) { service ->
                val isSelected = selectedService?.id == service.id
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onSelectService(service) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(service.title, fontWeight = FontWeight.Bold)
                            Text("$${service.basePrice}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        }
                        Text(service.description, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Complementary Parts & Check-ups", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(addOns) { addOn ->
                val isChecked = selectedAddOns.contains(addOn)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onToggleAddOn(addOn) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Checkbox(checked = isChecked, onCheckedChange = { onToggleAddOn(addOn) })
                        Column {
                            Text(addOn.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(addOn.description, fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Text(if (addOn.price > 0.0) "$${addOn.price}" else "FREE", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Total Estimated", fontSize = 12.sp, color = Color.Gray)
                Text("$${totalPrice}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Button(
                onClick = onProceedToCheckout,
                enabled = selectedService != null
            ) {
                Text("Review Booking")
            }
        }
    }
}

@Composable
fun CheckoutScreen(
    vehicle: Vehicle,
    service: ServiceItem?,
    addOns: List<AddOnPart>,
    totalPrice: Double,
    onConfirmBooking: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Review & Confirm", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Vehicle: ${vehicle.year} ${vehicle.make} ${vehicle.model}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Service: ${service?.title ?: "None"} ($${service?.basePrice ?: 0.0})")
                if (addOns.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Add-ons:", fontWeight = FontWeight.SemiBold)
                    addOns.forEach { addOn ->
                        Text("• ${addOn.name} ($${addOn.price})", fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Price:", fontWeight = FontWeight.Bold)
                    Text("$${totalPrice}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) { Text("Back") }
            Button(onClick = onConfirmBooking) { Text("Confirm & Book Service") }
        }
    }
}

@Composable
fun SuccessScreen(onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Account Verified & Ready!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your phone number has been successfully verified and authenticated.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) {
            Text("Proceed to Dashboard")
        }
    }
}
