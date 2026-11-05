# Ur Drive Client App
[![Android CI/CD](https://github.com/StevOti/ur_drive_client_app/actions/workflows/android_ci.yml/badge.svg)](https://github.com/StevOti/ur_drive_client_app/actions/workflows/android_ci.yml)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue.svg)](https://kotlinlang.org/docs/multiplatform.html)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-brightgreen.svg)](https://www.jetbrains.com/compose-multiplatform)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## Overview
**Ur Drive** is an advanced, enterprise-grade cross-platform vehicle fleet management and service booking ecosystem built with [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) and [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform). It empowers vehicle owners and fleet operators to seamlessly onboard their vehicles, configure precise specifications, track service histories, select customizable maintenance add-ons, and execute bookings backed by [Firebase](https://firebase.google.com/).

---

## Key Features

1. **Multi-Step Onboarding Wizard**:
   - Comprehensive profile setup (Personal details, DOB, ID, Address).
   - Dynamic vehicle brand & model selection driven by bundled East African/EPA vehicle datasets (`ea_cars.json`).
   - Detailed specification card previews (Transmission, Cylinders, Drive, ABS Status, Combined MPG).
2. **Garage & Fleet Management**:
   - Add, inspect, and manage multiple vehicles with brand-specific logos.
   - Real-time Firestore synchronization.
3. **Service Booking & Maintenance Hub**:
   - Browse specialized servicing packages with add-on parts.
   - Dynamic quote calculation and instant status tracking.
4. **CI/CD Automation**:
   - Automated [GitHub Actions](https://github.com/features/actions) pipeline ensuring continuous integration, compilation verification, and unit test execution on every push and pull request.

---

## Branching & Production Workflow
- **`main`**: The default production-ready branch. All tested and approved feature branches are merged here.
- **`authentication`**: Active feature development branch for authentication and onboarding workflows. Pull requests should target `main` upon verification.

---

## Architecture & Tech Stack

- **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform) (Material 3, declarative UI, custom carousels, responsive layouts).
- **Architecture**: MVVM with Unidirectional Data Flow (UDF), Kotlin Coroutines, and StateFlow.
- **Shared Core (`commonMain`)**: Domain models, repositories, and ViewModels shared across platforms.
- **Backend**: Firebase Authentication & Cloud Firestore.
- **Serialization**: Kotlinx Serialization & Kotlinx Datetime.
- **Platforms**: Android & iOS.

---

## Project Structure

```text
ur_drive_client_app/
├── androidApp/                 # Android native application entry point & Manifest
├── iosApp/                     # iOS native application entry point (SwiftUI / Xcode)
├── shared/                     # Kotlin Multiplatform shared module
│   ├── src/commonMain/         # Shared UI, ViewModels, Repositories, Domain models
│   │   ├── composeResources/   # Assets (Car brand logos, Lottie animations, ea_cars.json)
│   │   └── kotlin/             # Shared KMP source code
│   ├── src/androidMain/        # Android-specific platform implementations
│   └── src/iosMain/            # iOS-specific platform implementations
└── .github/workflows/          # GitHub Actions CI/CD pipelines
```

---

## Getting Started & Build Instructions

### Prerequisites
- Android Studio with [Kotlin Multiplatform plugin](https://kotlinlang.org/docs/multiplatform-mobile-get-started.html).
- JDK 17 or higher.
- Xcode (latest stable for iOS development).

### Build & Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/StevOti/ur_drive_client_app.git
   cd ur_drive_client_app
   ```
2. **Build Android App**:
   ```bash
   ./gradlew :androidApp:assembleDebug
   ```
3. **Run Unit Tests**:
   ```bash
   ./gradlew :shared:testDebugUnitTest
   ```

---

## Contribution Guidelines
1. Fork the repository and create your feature branch: `git checkout -b feature/YourFeatureName`
2. Commit your changes following conventional commits: `git commit -m "feat: add amazing new feature"`
3. Push to the branch: `git push origin feature/YourFeatureName`
4. Open a Pull Request targeting the **`main`** branch for production readiness.

---
*(c) 2025 Ur Drive Client App. All rights reserved.*
