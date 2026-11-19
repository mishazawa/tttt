# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Single-module Android app (`:app`, package `com.tttt.app`) in Kotlin + Jetpack Compose. It is developed in Claude Code cloud sessions with no device or emulator attached, so verification means compiling, running unit tests and lint. Builds and releases happen on GitHub Actions. The owner tests on a Pixel 3 XL running Android 12 (API 31), so `minSdk` must stay ≤ 31.

## Environment setup

`scripts/setup-android-env.sh` installs the Android SDK (default `/opt/android-sdk`, or `$ANDROID_HOME` if set) and writes `local.properties`. It is idempotent, and both CI workflows run it, so **when you change `compileSdk`, update `SDK_PACKAGES` in this script to match**. The cloud environment (`tttt`) runs this script as its setup script. The SDK download needs network access to `dl.google.com`.

Maven Central sometimes answers with HTTP 429 from the cloud container. If dependency resolution fails that way, rerun the command; it is not a build-config problem.

## Commands

```bash
./gradlew assembleDebug                 # debug APK -> app/build/outputs/apk/debug/
./gradlew testDebugUnitTest             # JVM unit tests
./gradlew testDebugUnitTest --tests 'com.tttt.app.ui.counter.CounterViewModelTest.clickIncrementsCounter'
./gradlew lintDebug                     # report in app/build/reports/
./gradlew assembleRelease               # R8-minified; signed only if keystore env vars are set
```

## Build configuration

- Toolchain: AGP 9.x with Gradle 9.x via the wrapper. Kotlin support is built into AGP 9, so do **not** apply `org.jetbrains.kotlin.android`; only the Compose compiler plugin is applied. All versions live in `gradle/libs.versions.toml`.
- `compileSdk` uses the AGP 9 minor-API DSL (`release(37) { minorApiLevel = 2 }`) because current AndroidX/Compose versions require API 37.1 or later. `targetSdk` is set separately.
- Versioning and signing come from environment variables in `app/build.gradle.kts`, not from hardcoded values:
  - `VERSION_NAME` (a leading `v` is stripped, so a tag name can be passed directly) and `VERSION_CODE`. Without them the defaults are `0.1.0-dev` and `1`.
  - `ANDROID_KEYSTORE_PATH`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS` and `ANDROID_KEY_PASSWORD`. Without these, the release build is unsigned.

## Code style and architecture

The owner wants code that a person can read easily and that follows standard Android best practices. Write Kotlin, not Java, including build scripts (`*.gradle.kts`).

The code follows the official Android app architecture guidance: one package per feature under `ui/`.
- `ui/<feature>/<Feature>ViewModel.kt`: an immutable `<Feature>UiState` data class, exposed as a `StateFlow` by a `ViewModel` with `on<Event>()` methods. It has no Android UI types, so it can be unit-tested on the JVM. Tests go in `app/src/test/.../ui/<feature>/`.
- `ui/<feature>/<Feature>Screen.kt` has two composables:
  - `<Feature>Route` gets the ViewModel and collects its state with `collectAsStateWithLifecycle`.
  - `<Feature>Screen` is stateless: it takes `uiState` and event lambdas, and has a `@Preview`.
- `ui/theme/Theme.kt` holds `TtttTheme`, which uses Material 3 with dynamic color on Android 12+.
- User-visible text goes in `res/values/strings.xml`. Counts use `<plurals>`, and Kotlin code never hardcodes UI text.
- Keep it as simple as the feature allows. Don't add DI frameworks, repositories or navigation until a feature actually needs them.

## CI / Release (`.github/workflows/`)

- `ci.yml`: runs on every branch push and PR. It builds, tests and lints, then uploads the debug APK and the reports as artifacts.
- `release.yml`: pushing a tag `vX.Y.Z` creates a GitHub Release with `tttt-vX.Y.Z-debug.apk` attached. It also attaches `tttt-vX.Y.Z.apk` (signed release) when the repository secrets `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS` and `ANDROID_KEY_PASSWORD` are set. `VERSION_CODE` is the workflow run number.
