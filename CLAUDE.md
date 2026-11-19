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
./gradlew testDebugUnitTest --tests 'com.tttt.app.CounterTest.clickIncrementsAndPluralizes'
./gradlew lintDebug                     # report in app/build/reports/
./gradlew assembleRelease               # R8-minified; signed only if keystore env vars are set
```

## Build configuration

- Toolchain: AGP 9.x with Gradle 9.x via the wrapper. Kotlin support is built into AGP 9, so do **not** apply `org.jetbrains.kotlin.android`; only the Compose compiler plugin is applied. All versions live in `gradle/libs.versions.toml`.
- `compileSdk` uses the AGP 9 minor-API DSL (`release(37) { minorApiLevel = 2 }`) because current AndroidX/Compose versions require API 37.1 or later. `targetSdk` is set separately.
- Versioning and signing come from environment variables in `app/build.gradle.kts`, not from hardcoded values:
  - `VERSION_NAME` (a leading `v` is stripped, so a tag name can be passed directly) and `VERSION_CODE`. Without them the defaults are `0.1.0-dev` and `1`.
  - `ANDROID_KEYSTORE_PATH`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS` and `ANDROID_KEY_PASSWORD`. Without these, the release build is unsigned.
- Keep UI state logic in plain Kotlin classes such as `Counter.kt`, with no Android types, so it can be unit-tested on the JVM. Composables in `MainActivity.kt` only render that state.

## CI / Release (`.github/workflows/`)

- `ci.yml`: runs on every branch push and PR. It builds, tests and lints, then uploads the debug APK and the reports as artifacts.
- `release.yml`: pushing a tag `vX.Y.Z` creates a GitHub Release with `tttt-vX.Y.Z-debug.apk` attached. It also attaches `tttt-vX.Y.Z.apk` (signed release) when the repository secrets `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS` and `ANDROID_KEY_PASSWORD` are set. `VERSION_CODE` is the workflow run number.
