# Ur Drive 🚗💨
### Vehicle Management & Service Booking Platform

**Ur Drive** is an enterprise-grade, cross-platform vehicle management and service booking application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. It allows vehicle owners to manage their garage, browse specialized maintenance services, complement them with custom parts and check-ups, calculate dynamic pricing, and book appointments seamlessly backed by **Firebase**.

---

## 🏗️ Architecture & Tech Stack

- **Frontend**: Compose Multiplatform (Material 3, AnimatedContent, declarative UI with Unidirectional Data Flow).
- **Shared Business Logic**: Kotlin Multiplatform (`commonMain`), MVVM Architecture with `StateFlow` and Coroutines.
- **Backend & Database**: Firebase Authentication & Cloud Firestore (via GitLive Firebase KMP SDK).
- **Serialization & Datetime**: Kotlinx Serialization & Kotlinx Datetime.
- **Targets**: Android & iOS.

---

## 📂 Project Structure

- `[/shared](./shared/src/commonMain/kotlin/com/urdrive/clientapp)`: Contains shared domain models, repositories, ViewModels, and Compose Multiplatform UI components.
  - `domain/model`: Data models (`Vehicle`, `ServiceItem`, `AddOnPart`, `Booking`).
  - `data/repository`: Firebase Firestore integrations (`VehicleRepository`, `BookingRepository`).
  - `presentation`: MVVM State holders (`GarageViewModel`, `ServiceBookingViewModel`).
- `[/androidApp](./androidApp)`: Android application entry point (`MainActivity`).
- `[/iosApp](./iosApp)`: iOS application entry point (SwiftUI / UIKit interop with Shared KMP framework).

---

## 🚀 Getting Started

### Prerequisites
- Android Studio / IntelliJ IDEA with KMP and Compose Multiplatform support.
- JDK 17.
- Xcode (for iOS target).

### Running the Apps
- **Android App**: `./gradlew :androidApp:assembleDebug`
- **iOS App**: Open `/iosApp` in Xcode and run on simulator or device.

### Running Tests
- **Host Unit Tests**: `./gradlew :shared:testAndroidHostTest`

---

## 🛡️ Engineering Principles
- **Clean Architecture & UDF**: Strict separation between UI, presentation (ViewModels), domain, and data layers.
- **Security**: Secure Firestore data access rules and validated authentication state handling.
- **Performance**: Optimized recompositions and asynchronous coroutine dispatchers for lightning-fast client retention.
