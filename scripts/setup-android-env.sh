#!/usr/bin/env bash
# Installs the Android SDK needed to build this project (idempotent).
# Usage: scripts/setup-android-env.sh        (from anywhere; run as root in cloud envs)
# Requires: JDK 17+ on PATH, network access to dl.google.com.
set -euo pipefail

CMDLINE_TOOLS_BUILD="${CMDLINE_TOOLS_BUILD:-13114758}"
SDK_PACKAGES=("platform-tools" "platforms;android-37.2" "build-tools;36.0.0")

ANDROID_HOME="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-/opt/android-sdk}}"
export ANDROID_HOME ANDROID_SDK_ROOT="$ANDROID_HOME"
SDKMANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager"

if ! command -v java >/dev/null; then
  echo "java not found; install a JDK 17+ first" >&2; exit 1
fi

if [ ! -x "$SDKMANAGER" ]; then
  echo "Installing Android command-line tools ($CMDLINE_TOOLS_BUILD) into $ANDROID_HOME"
  tmp="$(mktemp -d)"
  curl -fsSL -o "$tmp/cmdline-tools.zip" \
    "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_BUILD}_latest.zip"
  mkdir -p "$ANDROID_HOME/cmdline-tools"
  unzip -q "$tmp/cmdline-tools.zip" -d "$tmp"
  rm -rf "$ANDROID_HOME/cmdline-tools/latest"
  mv "$tmp/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest"
  rm -rf "$tmp"
fi

yes | "$SDKMANAGER" --licenses >/dev/null || true
"$SDKMANAGER" --install "${SDK_PACKAGES[@]}" >/dev/null

# Point Gradle at the SDK for this checkout (local.properties is gitignored).
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "sdk.dir=$ANDROID_HOME" > "$REPO_ROOT/local.properties"

# Persist env vars for future shells when we can.
if [ -w /etc/profile.d ]; then
  cat > /etc/profile.d/android-sdk.sh <<PROFILE
export ANDROID_HOME=$ANDROID_HOME
export ANDROID_SDK_ROOT=$ANDROID_HOME
export PATH=\$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
PROFILE
fi

echo "Android SDK ready at $ANDROID_HOME"
