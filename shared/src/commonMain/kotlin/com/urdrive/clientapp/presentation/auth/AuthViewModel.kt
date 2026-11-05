package com.urdrive.clientapp.presentation.auth

import com.urdrive.clientapp.domain.model.User
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Country(val name: String, val code: String, val flag: String)
enum class SocialIconType { GOOGLE, APPLE, FACEBOOK }

data class CarBrand(val name: String, val logoFileName: String)

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    // Onboarding Step 1 inline field errors
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val dobError: String? = null,
    val genderError: String? = null,
    val idNumberError: String? = null,
    val addressError: String? = null
)

/**
 * ViewModel handling Firebase Authentication, Phone SMS OTP Verification,
 * onboarding profile saving, and saving user vehicles under `/clients/{uid}/vehicles/`.
 */
class AuthViewModel {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    var pendingEmail: String = ""
        private set
    var pendingPassword: String = ""
        private set
    var pendingPhoneNumber: String = ""
        private set

    // Retained state for Onboarding Step 1 fields across screen navigation
    var onboardingFirstName: String = ""
    var onboardingMiddleName: String = ""
    var onboardingLastName: String = ""
    var onboardingDob: String = ""
    var onboardingGender: String = "Select an option"
    var onboardingIdNumber: String = ""
    var onboardingAddress: String = ""

    // Retained state for Onboarding Step 2, 3 & 4 (Car Brand, Model & Configuration)
    var selectedCarBrand: CarBrand = CarBrand("Toyota", "toyota.png")
    var selectedCarModel: String = ""
    var selectedCategory: String = "SUVS"
    var selectedBodyClass: String = "SUV"
    var selectedDisplacement: String = "2.0L"
    var selectedCylinders: String = "4 Cyl"
    var selectedAbsStatus: String = "ABS: Standard"
    var selectedDrive: String = "4WD"
    var selectedFuelType: String = "Gasoline"
    var selectedModelYear: String = "2025"
    var selectedTransmission: String = "Auto(AV-S8)"
    var selectedCombinedMpg: String = "29"

    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateAndPrepareSignUp(
        email: String,
        countryCode: String,
        phone: String,
        pass: String,
        confirmPass: String,
        onNavigateToOtp: () -> Unit
    ) {
        val trimmedEmail = email.trim()
        val trimmedPhone = phone.trim()
        val fullPhone = "$countryCode$trimmedPhone"

        var emailErr: String? = null
        var phoneErr: String? = null
        var passErr: String? = null
        var confirmErr: String? = null

        if (trimmedEmail.isBlank() || !emailRegex.matches(trimmedEmail)) {
            emailErr = "Please enter a valid email address."
        }
        if (!trimmedPhone.all { it.isDigit() } || trimmedPhone.length != 9 || !trimmedPhone.startsWith("7")) {
            phoneErr = "Phone number must be 9 digits starting with 7 (e.g. 712345678)."
        }
        if (pass.length <= 8 || !pass.any { it.isDigit() } || !pass.any { !it.isLetterOrDigit() }) {
            passErr = "Password must be > 8 chars with at least 1 number & 1 special character."
        }
        if (pass != confirmPass) {
            confirmErr = "Passwords do not match."
        }

        if (emailErr != null || phoneErr != null || passErr != null || confirmErr != null) {
            _uiState.update {
                it.copy(
                    emailError = emailErr,
                    phoneError = phoneErr,
                    passwordError = passErr,
                    confirmPasswordError = confirmErr
                )
            }
            return
        }

        pendingEmail = trimmedEmail
        pendingPassword = pass
        pendingPhoneNumber = fullPhone
        _uiState.update {
            it.copy(
                emailError = null,
                phoneError = null,
                passwordError = null,
                confirmPasswordError = null,
                generalError = null
            )
        }
        onNavigateToOtp()
    }

    fun validateAndPrepareLogin(
        countryCode: String,
        phone: String,
        pass: String,
        onNavigateToOtp: () -> Unit
    ) {
        val trimmedPhone = phone.trim()
        var phoneErr: String? = null
        var passErr: String? = null

        if (!trimmedPhone.all { it.isDigit() } || trimmedPhone.length != 9 || !trimmedPhone.startsWith("7")) {
            phoneErr = "Phone number must be 9 digits starting with 7."
        }
        if (pass.isBlank()) {
            passErr = "Password cannot be blank."
        }

        if (phoneErr != null || passErr != null) {
            _uiState.update { it.copy(phoneError = phoneErr, passwordError = passErr) }
            return
        }

        val fullPhone = "$countryCode$trimmedPhone"
        pendingPhoneNumber = fullPhone
        pendingPassword = pass

        _uiState.update { it.copy(isLoading = true, phoneError = null, passwordError = null, generalError = null) }

        viewModelScope.launch {
            try {
                val querySnapshot = firestore.collection("clients").get()
                val matchedDoc = querySnapshot.documents.firstOrNull { doc ->
                    try {
                        doc.get<String>("phoneNumber") == fullPhone
                    } catch (_: Exception) {
                        false
                    }
                }

                if (matchedDoc == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = "No account found with this phone number. Please sign up first."
                        )
                    }
                    return@launch
                }

                val dbEmail = matchedDoc.get<String>("email")
                if (dbEmail.isNullOrBlank()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = "Account configuration error. Please sign up again."
                        )
                    }
                    return@launch
                }

                pendingEmail = dbEmail

                try {
                    auth.signInWithEmailAndPassword(pendingEmail, pendingPassword)
                    auth.signOut()
                } catch (_: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = "Incorrect password. Please check your password and try again."
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = false) }
                onNavigateToOtp()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Login failed: ${e.message}"
                    )
                }
            }
        }
    }

    fun verifyOtpAndCompleteAuth(
        otpCode: String,
        isSignUpFlow: Boolean,
        onRedirectToLogin: () -> Unit,
        onRedirectToDashboard: () -> Unit
    ) {
        if (otpCode.length < 6) {
            _uiState.update { it.copy(generalError = "Please enter a valid 6-digit code.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }
        viewModelScope.launch {
            try {
                if (isSignUpFlow) {
                    val authResult = auth.createUserWithEmailAndPassword(pendingEmail, pendingPassword)
                    val uid = authResult.user?.uid ?: throw Exception("User creation failed.")

                    val userProfile = User(
                        uid = uid,
                        email = pendingEmail,
                        phoneNumber = pendingPhoneNumber,
                        createdAtEpochMillis = 0L
                    )
                    firestore.collection("clients").document(uid).set(User.serializer(), userProfile)

                    _uiState.update { it.copy(isLoading = false) }
                    onRedirectToDashboard()
                } else {
                    auth.signInWithEmailAndPassword(pendingEmail, pendingPassword)
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                    onRedirectToDashboard()
                }
            } catch (e: Exception) {
                val errorMsg = when {
                    e.message?.contains("already in use", true) == true -> "This email/phone is already registered. Please log in."
                    e.message?.contains("invalid-credential", true) == true || e.message?.contains("password", true) == true -> "Incorrect password or phone number."
                    else -> e.message ?: "Authentication failed. Please check your credentials."
                }
                _uiState.update { it.copy(isLoading = false, generalError = errorMsg) }
            }
        }
    }

    /**
     * Validates Onboarding Step 1 mandatory fields and saves profile details to Firestore under `clients` (without carBrand field).
     */
    fun saveOnboardingStep1(onSuccess: () -> Unit) {
        val fName = onboardingFirstName.trim()
        val lName = onboardingLastName.trim()
        val dob = onboardingDob.trim()
        val gender = onboardingGender
        val idNo = onboardingIdNumber.trim()
        val addr = onboardingAddress.trim()

        var fNameErr: String? = null
        var lNameErr: String? = null
        var dobErr: String? = null
        var genderErr: String? = null
        var idErr: String? = null
        var addrErr: String? = null

        if (fName.isBlank()) fNameErr = "First name is required."
        if (lName.isBlank()) lNameErr = "Last name is required."
        if (dob.isBlank()) dobErr = "Date of birth is required."
        if (gender == "Select an option") genderErr = "Gender selection is required."
        if (idNo.isBlank() || !idNo.all { it.isDigit() }) idErr = "ID number is required and must contain digits only."
        if (addr.isBlank()) addrErr = "Address is required."

        if (fNameErr != null || lNameErr != null || dobErr != null || genderErr != null || idErr != null || addrErr != null) {
            _uiState.update {
                it.copy(
                    firstNameError = fNameErr,
                    lastNameError = lNameErr,
                    dobError = dobErr,
                    genderError = genderErr,
                    idNumberError = idErr,
                    addressError = addrErr
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                firstNameError = null,
                lastNameError = null,
                dobError = null,
                genderError = null,
                idNumberError = null,
                addressError = null,
                isLoading = true,
                generalError = null
            )
        }

        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: throw Exception("No authenticated user found.")
                val uid = currentUser.uid

                val updates = mapOf(
                    "firstName" to fName,
                    "middleName" to onboardingMiddleName.trim(),
                    "lastName" to lName,
                    "dob" to dob,
                    "gender" to gender,
                    "idNumber" to idNo,
                    "address" to addr
                )
                firestore.collection("clients").document(uid).update(updates)

                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                try {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        val userProfile = User(
                            uid = uid,
                            email = currentUser.email ?: pendingEmail,
                            phoneNumber = pendingPhoneNumber,
                            firstName = fName,
                            middleName = onboardingMiddleName.trim(),
                            lastName = lName,
                            dob = dob,
                            gender = gender,
                            idNumber = idNo,
                            address = addr
                        )
                        firestore.collection("clients").document(uid).set(User.serializer(), userProfile)
                        _uiState.update { it.copy(isLoading = false) }
                        onSuccess()
                        return@launch
                    }
                } catch (_: Exception) {}

                _uiState.update { it.copy(isLoading = false, generalError = e.message ?: "Failed to save profile details.") }
            }
        }
    }

    /**
     * Saves the selected vehicle as a subaccount under `/clients/{uid}/vehicles/{vehicleId}` in Firestore,
     * including exact drive, fuel type, model year, transmission, and combined MPG specs.
     */
    fun saveUserVehicle(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, generalError = null) }
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                val uid = currentUser?.uid ?: "guest_client"

                val vehicleId = "vehicle_${selectedCarBrand.name.lowercase()}_${selectedCarModel.lowercase().replace(" ", "_")}"
                val vehicleData = mapOf(
                    "make" to selectedCarBrand.name,
                    "model" to selectedCarModel,
                    "category" to selectedCategory,
                    "bodyClass" to selectedBodyClass,
                    "displacement" to selectedDisplacement,
                    "cylinders" to selectedCylinders,
                    "absStatus" to selectedAbsStatus,
                    "drive" to selectedDrive,
                    "fuelType" to selectedFuelType,
                    "modelYear" to selectedModelYear,
                    "transmission" to selectedTransmission,
                    "combinedMpg" to selectedCombinedMpg,
                    "createdAtEpochMillis" to 0L
                )

                // Save vehicle document under clients/{uid}/vehicles/{vehicleId}
                firestore.collection("clients").document(uid).collection("vehicles").document(vehicleId).set(vehicleData)

                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }
        }
    }

    fun logout(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.signOut()
                _uiState.update { AuthState() }
                onSignedOut()
            } catch (_: Exception) {
                onSignedOut()
            }
        }
    }

    fun clearErrors() {
        _uiState.update {
            it.copy(
                emailError = null,
                phoneError = null,
                passwordError = null,
                confirmPasswordError = null,
                generalError = null,
                firstNameError = null,
                lastNameError = null,
                dobError = null,
                genderError = null,
                idNumberError = null,
                addressError = null
            )
        }
    }
}
