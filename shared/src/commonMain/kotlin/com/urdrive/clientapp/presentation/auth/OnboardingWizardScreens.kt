package com.urdrive.clientapp.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import ur_drive_client_app.shared.generated.resources.Res

/**
 * Reusable Multi-Step Progress Bar (Carousel Slider Indicator) for Onboarding Wizard.
 */
@Composable
fun OnboardingProgressBar(currentStep: Int, totalSteps: Int = 5) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isActive = i <= currentStep
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isActive) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f))
            )
        }
    }
}

/**
 * Step 1: "Tell us about yourself 👨‍💻"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep1Screen(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var firstName by remember { mutableStateOf(viewModel.onboardingFirstName) }
    var middleName by remember { mutableStateOf(viewModel.onboardingMiddleName) }
    var lastName by remember { mutableStateOf(viewModel.onboardingLastName) }
    var birthday by remember { mutableStateOf(viewModel.onboardingDob) }
    var gender by remember { mutableStateOf(viewModel.onboardingGender) }
    var idNumber by remember { mutableStateOf(viewModel.onboardingIdNumber) }
    var address by remember { mutableStateOf(viewModel.onboardingAddress) }

    var genderExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val uiState by viewModel.uiState.collectAsState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val dateTime = instant.toLocalDateTime(TimeZone.UTC)
                            birthday = "${dateTime.day.toString().padStart(2, '0')} / ${dateTime.month.toString().padStart(2, '0')} / ${dateTime.year}"
                            viewModel.onboardingDob = birthday
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OnboardingProgressBar(currentStep = 1, totalSteps = 5)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tell us about yourself 😎",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(24.dp))

                // First Name and Last Name grouped in a single row with sharp corners
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "First name *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = firstName,
                            onValueChange = {
                                firstName = it
                                viewModel.onboardingFirstName = it
                            },
                            placeholder = { Text("First name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            isError = uiState.firstNameError != null
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Last name *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = lastName,
                            onValueChange = {
                                lastName = it
                                viewModel.onboardingLastName = it
                            },
                            placeholder = { Text("Last name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            isError = uiState.lastNameError != null
                        )
                    }
                }
                if (uiState.firstNameError != null || uiState.lastNameError != null) {
                    Text(
                        text = uiState.firstNameError ?: uiState.lastNameError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Middle Name (Optional)
                Text(text = "Middle name (optional)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = middleName,
                    onValueChange = {
                        middleName = it
                        viewModel.onboardingMiddleName = it
                    },
                    placeholder = { Text("Enter middle name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Date of Birth
                Text(text = "Date of birth *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { showDatePicker = true },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = birthday.ifBlank { "dd / mm / yyyy" },
                                color = if (birthday.isBlank()) Color.Gray.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onBackground,
                                fontSize = 15.sp
                            )
                            Text(text = "📅", fontSize = 16.sp)
                        }
                    }
                }
                if (uiState.dobError != null) {
                    Text(text = uiState.dobError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gender
                Text(text = "Your gender *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { genderExpanded = true },
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = gender, fontSize = 15.sp)
                            Text(text = "▼", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    DropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        listOf("Female", "Male", "Non-binary", "Prefer not to say").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    viewModel.onboardingGender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }
                if (uiState.genderError != null) {
                    Text(text = uiState.genderError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ID Number
                Text(text = "ID number *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = idNumber,
                    onValueChange = {
                        if (it.all { ch -> ch.isDigit() }) {
                            idNumber = it
                            viewModel.onboardingIdNumber = it
                        }
                    },
                    placeholder = { Text("Enter ID number (digits only)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.idNumberError != null
                )
                if (uiState.idNumberError != null) {
                    Text(text = uiState.idNumberError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Address
                Text(text = "Address *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = address,
                    onValueChange = {
                        address = it
                        viewModel.onboardingAddress = it
                    },
                    placeholder = { Text("Enter your physical address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    isError = uiState.addressError != null
                )
                if (uiState.addressError != null) {
                    Text(text = uiState.addressError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }

                if (uiState.generalError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = uiState.generalError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        viewModel.saveOnboardingStep1(onSuccess = onNext)
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RectangleShape,
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569))
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Next", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onBack) {
                    Text("Back", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
            }
        }
    }
}

/**
 * Loads and displays an actual car brand logo image from `files/car_brand_logos/` folder,
 * with graceful fallback to car icon (🚗) if the logo file doesn't exist yet.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun CarBrandLogoImage(logoFileName: String, modifier: Modifier = Modifier) {
    var imageBitmap by remember(logoFileName) { mutableStateOf<ImageBitmap?>(null) }
    var hasError by remember(logoFileName) { mutableStateOf(false) }

    LaunchedEffect(logoFileName) {
        try {
            val bytes = Res.readBytes("files/car_brand_logos/$logoFileName")
            imageBitmap = bytes.decodeToImageBitmap()
        } catch (_: Exception) {
            hasError = true
        }
    }

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        if (imageBitmap != null && !hasError) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp)
            )
        } else {
            Text(text = "🚗", fontSize = 22.sp)
        }
    }
}

@Serializable
data class EaCarMake(
    val make: String = "",
    val logoFileName: String = "",
    val models: List<EaCarModel> = emptyList()
)

@Serializable
data class EaCarModel(
    val name: String = "",
    val category: String = "SUVS",
    val tagline: String = "",
    val bodyClass: String = "",
    val displacement: String = "",
    val cylinders: String = "",
    val absStatus: String = "",
    val fuelEconomyUS: List<EaFuelEconomyUS> = emptyList(),
    val epaRatings: List<EaEpaRating> = emptyList()
)

@Serializable
data class EaFuelEconomyUS(
    val modelYear: String = "2025",
    val transmission: String = "Auto(AV-S8)",
    val fuelType: String = "Gasoline",
    val displacement: String = "",
    val cylinders: String = "",
    val combinedMpg: String = "29",
    val cityMpg: String = "22",
    val hwyMpg: String = "31",
    val annualFuelCostUSD: String = "2500",
    val ghgRating: String = "5",
    val notes: String? = null
)

@Serializable
data class EaEpaRating(
    val displ: String = "",
    val cyl: String = "",
    val trans: String = "",
    val drive: String = "4WD",
    val fuel: String = "Gasoline",
    val certRegion: String = "",
    val standard: String = "",
    val standardDesc: String = "",
    val underhoodId: String = "",
    val vehClass: String = "",
    val airPollutionScore: String = "5",
    val cityMpg: String = "22",
    val hwyMpg: String = "31",
    val combinedMpg: String = "25",
    val greenhouseGasScore: String = "5",
    val smartWay: String = "No",
    val combCO2: String = "300",
    val modelYear: String = "2025",
    val model: String = "",
    val source: String = ""
)

data class CarModelOption(
    val modelName: String,
    val category: String,
    val tagline: String,
    val bodyClass: String,
    val displacement: String,
    val cylinders: String,
    val absStatus: String,
    val drive: String,
    val fuelType: String,
    val modelYear: String,
    val transmission: String,
    val combinedMpg: String
) {
    val features: List<String>
        get() = listOf(category, displacement, cylinders)
}

/**
 * Step 2: "Select your car brand" - Dynamically fetches all brands from files/vehicle_data/ea_cars.json
 */
@Composable
fun OnboardingStep2Screen(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var carBrands by remember { mutableStateOf<List<CarBrand>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedBrand by remember { mutableStateOf(viewModel.selectedCarBrand) }
    var selectionError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val bytes = Res.readBytes("files/vehicle_data/ea_cars.json")
                val jsonString = bytes.decodeToString()
                val jsonParser = Json { ignoreUnknownKeys = true; isLenient = true; coerceInputValues = true }
                val makes = jsonParser.decodeFromString<List<EaCarMake>>(jsonString)

                val brands = makes.map { m ->
                    CarBrand(
                        name = m.make,
                        logoFileName = m.logoFileName.ifBlank { "${m.make.lowercase().replace(" ", "").replace("-", "")}.png" }
                    )
                }.filter { it.name.isNotBlank() }.distinctBy { it.name }

                withContext(Dispatchers.Main) {
                    carBrands = brands
                    if (brands.isNotEmpty() && (viewModel.selectedCarBrand.name.isBlank() || brands.none { it.name == selectedBrand.name })) {
                        selectedBrand = brands.first()
                        viewModel.selectedCarBrand = selectedBrand
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    loadError = "Error loading ea_cars.json: ${e::class.simpleName} - ${e.message}"
                    isLoading = false
                }
            }
        }
    }

    val filteredBrands = remember(searchQuery, carBrands) {
        if (searchQuery.isBlank()) carBrands
        else carBrands.filter { it.name.contains(searchQuery, ignoreCase = true) }
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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- STICKY TOP HEADER ---
            Column(modifier = Modifier.fillMaxWidth()) {
                OnboardingProgressBar(currentStep = 2, totalSteps = 5)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieHeaderView(
                            primaryPath = "drawable/car_brand.json",
                            altPath = "drawable/female_avatar.json",
                            fallbackEmoji = "🚗"
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Select your car brand",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Select the car brand to select the car model",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search car brand...") },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RectangleShape),
                        shape = RectangleShape,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RectangleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    ) {
                        Text("🔍", fontSize = 18.sp)
                    }
                }

                if (selectionError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = selectionError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- SCROLLABLE LIST SECTION IN THE MIDDLE ---
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                loadError != null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = loadError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredBrands) { brand ->
                            val isSelected = selectedBrand.name == brand.name
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedBrand = brand
                                        viewModel.selectedCarBrand = brand
                                        selectionError = null
                                    },
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        CarBrandLogoImage(logoFileName = brand.logoFileName)

                                        Column {
                                            Text(brand.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("Select the car brand to select the car model", fontSize = 11.sp, color = Color.Gray)
                                        }
                                    }
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            selectedBrand = brand
                                            viewModel.selectedCarBrand = brand
                                            selectionError = null
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- STICKY BOTTOM BUTTONS ---
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (selectedBrand.name.isBlank()) {
                            selectionError = "Please select a car brand to continue."
                        } else {
                            viewModel.selectedCarBrand = selectedBrand
                            onNext()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569))
                ) {
                    Text("Next", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onBack) {
                    Text("Back", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
            }
        }
    }
}

/**
 * Step 3: "Select your car model" - Dynamically fetches models for the selected brand from files/vehicle_data/ea_cars.json
 */
@Composable
fun OnboardingStep3Screen(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val selectedBrand = viewModel.selectedCarBrand

    var carModels by remember { mutableStateOf<List<CarModelOption>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedModel by remember { mutableStateOf(viewModel.selectedCarModel) }
    var searchQuery by remember { mutableStateOf("") }
    var selectionError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedBrand.name) {
        isLoading = true
        withContext(Dispatchers.IO) {
            try {
                val bytes = Res.readBytes("files/vehicle_data/ea_cars.json")
                val jsonString = bytes.decodeToString()
                val jsonParser = Json { ignoreUnknownKeys = true; isLenient = true; coerceInputValues = true }
                val makes = jsonParser.decodeFromString<List<EaCarMake>>(jsonString)

                val normalizedSelectedBrand = selectedBrand.name.lowercase().replace("-", "").replace(" ", "")
                val matchedMake = makes.firstOrNull { makeObj ->
                    val normMake = makeObj.make.lowercase().replace("-", "").replace(" ", "")
                    normMake == normalizedSelectedBrand || makeObj.make.equals(selectedBrand.name, ignoreCase = true)
                }

                val rawModels = matchedMake?.models ?: emptyList()
                val options = rawModels.map { m ->
                    val rating = m.epaRatings.firstOrNull()
                    val fe = m.fuelEconomyUS.firstOrNull()

                    val d = rating?.drive?.ifBlank { null } ?: "4WD"
                    val f = rating?.fuel?.ifBlank { null } ?: fe?.fuelType?.ifBlank { null } ?: "Gasoline"
                    val yr = rating?.modelYear?.toString()?.ifBlank { null } ?: fe?.modelYear?.toString()?.ifBlank { null } ?: "2025"
                    val tr = rating?.trans?.ifBlank { null } ?: fe?.transmission?.ifBlank { null } ?: "Auto(AV-S8)"
                    val mpg = rating?.combinedMpg?.toString() ?: fe?.combinedMpg?.toString() ?: "29"

                    CarModelOption(
                        modelName = m.name,
                        category = m.category.ifBlank { "SUVS" },
                        tagline = m.tagline.ifBlank { "${selectedBrand.name} ${m.name} model configuration." },
                        bodyClass = m.bodyClass.ifBlank { "SUV" },
                        displacement = m.displacement.ifBlank { "2.0L" },
                        cylinders = m.cylinders.ifBlank { "4 Cyl" },
                        absStatus = m.absStatus.ifBlank { "ABS: Standard" },
                        drive = d,
                        fuelType = f,
                        modelYear = yr,
                        transmission = tr,
                        combinedMpg = mpg
                    )
                }

                withContext(Dispatchers.Main) {
                    carModels = options
                    if (options.isNotEmpty()) {
                        if (viewModel.selectedCarModel.isBlank() || options.none { it.modelName == selectedModel }) {
                            selectedModel = options.first().modelName
                            viewModel.selectedCarModel = selectedModel
                            viewModel.selectedCategory = options.first().category
                            viewModel.selectedBodyClass = options.first().bodyClass
                            viewModel.selectedDisplacement = options.first().displacement
                            viewModel.selectedCylinders = options.first().cylinders
                            viewModel.selectedAbsStatus = options.first().absStatus
                            viewModel.selectedDrive = options.first().drive
                            viewModel.selectedFuelType = options.first().fuelType
                            viewModel.selectedModelYear = options.first().modelYear
                            viewModel.selectedTransmission = options.first().transmission
                            viewModel.selectedCombinedMpg = options.first().combinedMpg
                        }
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    carModels = emptyList()
                    isLoading = false
                }
            }
        }
    }

    val filteredModels = remember(searchQuery, carModels) {
        if (searchQuery.isBlank()) carModels
        else carModels.filter { it.modelName.contains(searchQuery, ignoreCase = true) || it.tagline.contains(searchQuery, ignoreCase = true) || it.features.any { f -> f.contains(searchQuery, ignoreCase = true) } }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- STICKY TOP HEADER ---
            Column(modifier = Modifier.fillMaxWidth()) {
                OnboardingProgressBar(currentStep = 3, totalSteps = 5)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CarBrandLogoImage(logoFileName = selectedBrand.logoFileName)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedBrand.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Select your car model",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search model (e.g. Forester, Fielder)...") },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RectangleShape),
                        shape = RectangleShape,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RectangleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    ) {
                        Text("🔍", fontSize = 18.sp)
                    }
                }

                if (selectionError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = selectionError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- SCROLLABLE LIST SECTION IN THE MIDDLE ---
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredModels) { modelOpt ->
                        val isSelected = selectedModel == modelOpt.modelName
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedModel = modelOpt.modelName
                                    viewModel.selectedCarModel = modelOpt.modelName
                                    viewModel.selectedCategory = modelOpt.category
                                    viewModel.selectedBodyClass = modelOpt.bodyClass
                                    viewModel.selectedDisplacement = modelOpt.displacement
                                    viewModel.selectedCylinders = modelOpt.cylinders
                                    viewModel.selectedAbsStatus = modelOpt.absStatus
                                    viewModel.selectedDrive = modelOpt.drive
                                    viewModel.selectedFuelType = modelOpt.fuelType
                                    viewModel.selectedModelYear = modelOpt.modelYear
                                    viewModel.selectedTransmission = modelOpt.transmission
                                    viewModel.selectedCombinedMpg = modelOpt.combinedMpg
                                    selectionError = null
                                },
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Same car icon displayed on top image of car model screens
                                    CarBrandLogoImage(logoFileName = selectedBrand.logoFileName, modifier = Modifier.size(42.dp))

                                    Column {
                                        Text(modelOpt.modelName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(modelOpt.tagline, fontSize = 11.sp, color = Color.Gray)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        // Exactly 3 badges: category, displacement, cylinders
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            modelOpt.features.forEach { feature ->
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                ) {
                                                    Text(
                                                        text = feature,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        selectedModel = modelOpt.modelName
                                        viewModel.selectedCarModel = modelOpt.modelName
                                        viewModel.selectedCategory = modelOpt.category
                                        viewModel.selectedBodyClass = modelOpt.bodyClass
                                        viewModel.selectedDisplacement = modelOpt.displacement
                                        viewModel.selectedCylinders = modelOpt.cylinders
                                        viewModel.selectedAbsStatus = modelOpt.absStatus
                                        viewModel.selectedDrive = modelOpt.drive
                                        viewModel.selectedFuelType = modelOpt.fuelType
                                        viewModel.selectedModelYear = modelOpt.modelYear
                                        viewModel.selectedTransmission = modelOpt.transmission
                                        viewModel.selectedCombinedMpg = modelOpt.combinedMpg
                                        selectionError = null
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- STICKY BOTTOM BUTTONS ---
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (selectedModel.isBlank()) {
                            selectionError = "Please select a car model to continue."
                        } else {
                            viewModel.selectedCarModel = selectedModel
                            onNext()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569))
                ) {
                    Text("Next", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onBack) {
                    Text("Back", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
            }
        }
    }
}

/**
 * Step 4: Vehicle Configuration Summary Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep4Screen(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var fuelType by remember { mutableStateOf(viewModel.selectedFuelType) }
    var modelYear by remember { mutableStateOf(viewModel.selectedModelYear) }

    var showFuelSheet by remember { mutableStateOf(false) }
    var showYearSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // BottomSheet for Fuel Type
    if (showFuelSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFuelSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                Text("Select Fuel Type", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                listOf("Gasoline", "Diesel", "Electric", "Hybrid", "Petrol").forEach { fuel ->
                    TextButton(
                        onClick = {
                            fuelType = fuel
                            viewModel.selectedFuelType = fuel
                            showFuelSheet = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(fuel, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // BottomSheet for Model Year (Grid-style)
    if (showYearSheet) {
        ModalBottomSheet(
            onDismissRequest = { showYearSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                Text("Select Model Year", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                val years = listOf("2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020")
                years.chunked(2).forEach { rowYears ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowYears.forEach { yr ->
                            Button(
                                onClick = {
                                    modelYear = yr
                                    viewModel.selectedModelYear = yr
                                    showYearSheet = false
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RectangleShape
                            ) {
                                Text(yr, fontSize = 15.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OnboardingProgressBar(currentStep = 4, totalSteps = 5)
                Spacer(modifier = Modifier.height(16.dp))

                // Hero Image Card (16:9 header image styling)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Surface(
                                shape = RectangleShape,
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = "NEW VEHICLE ADDED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${viewModel.selectedCarBrand.name} ${viewModel.selectedCarModel.ifBlank { "Vehicle" }}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Config Cards side-by-side: "Fuel Type" and "Model Year"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fuel Type Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showFuelSheet = true },
                        shape = RectangleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("FUEL TYPE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⛽ $fuelType", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("▼", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }

                    // Model Year Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showYearSheet = true },
                        shape = RectangleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("MODEL YEAR", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📅 $modelYear", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("▼", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Key Specifications Section
                Text(
                    text = "KEY SPECIFICATIONS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Transmission
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("⚙️", fontSize = 16.sp)
                                Text("Transmission", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Text(viewModel.selectedTransmission.ifBlank { "Auto(AV-S8)" }, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                        // Cylinders
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("🔋", fontSize = 16.sp)
                                Text("Cylinders", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Text(viewModel.selectedCylinders.ifBlank { "4 Cyl" }, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                        // Combined MPG
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("⛽", fontSize = 16.sp)
                                Text("Combined MPG", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Text("${viewModel.selectedCombinedMpg} MPG", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                        // Drive
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("🛞", fontSize = 16.sp)
                                Text("Drive", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Text(viewModel.selectedDrive.ifBlank { "4WD" }, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                        // ABS Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("🛡️", fontSize = 16.sp)
                                Text("ABS Status", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Text(viewModel.selectedAbsStatus.ifBlank { "ABS: Standard" }, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Success Alert Box (Emerald-tinted box)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B).copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✅", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "Configuration Confirmed",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Your vehicle profile has been successfully generated. You can now start managing your fleet.",
                                fontSize = 12.sp,
                                color = Color.LightGray.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Bottom Actions ("Get Started" saves vehicle to Firestore, then advances to Terms & Conditions)
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.saveUserVehicle {
                            onNext()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Get Started", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onBack) {
                    Text("Back", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
            }
        }
    }
}

/**
 * Step 5: "Terms & Conditions" (Final screen before login redirection).
 */
@Composable
fun TermsConditionsScreen(
    onAgreeAndContinue: () -> Unit,
    onDecline: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().safeContentPadding().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OnboardingProgressBar(currentStep = 5, totalSteps = 5)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "📄", fontSize = 28.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Terms & Conditions",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Update 1/8/2022",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "1. Introduction",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ad veniam reprehenderit exercitation aute deserunt minim veniam. Voluptate irure nisi consequat do ad aliquip nisi.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "2. Who can use this app?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ut nostrud cupidatat elit est eiusmod voluptate dolore nisi. Eiusmod deserunt sit excepteur nisi reprehenderit dolor labore magna pariatur nisi.\n\nElit deserunt cupidatat anim consequat reprehenderit voluptate. Labore labout non consequat ut voluptate reprehenderit est. Nostrud dolore et magna tempor co",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onAgreeAndContinue,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Agree and continue", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RectangleShape
                ) {
                    Text("Decline", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}
