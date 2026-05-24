# PokéFi

A tiny offline Android app that turns nearby Wi‑Fi networks into collectible monsters.

## What it does

- Scans real Wi‑Fi networks around you
- Maps each network to a monster name, rarity, and element
- Opens a capture screen with a Pokéball throw animation
- Works offline
- Uses no internet permission
- Requires no root

## Build it on GitHub

1. Create a new GitHub repository.
2. Copy these files into the repo and push them to `main`.
3. Open the **Actions** tab.
4. Select **Build APK**.
5. Click **Run workflow**.

When the workflow finishes, download the artifact named **PokéFi-debug-apk**. Inside is `app-debug.apk`.

## Install on your phone

1. Copy the APK to your Android phone.
2. Enable installing apps from unknown sources for the app you use to open the APK.
3. Tap the APK to install it.

## Notes

- Wi‑Fi scanning needs location permission on Android devices.
- On Android 13 and newer, the app also asks for the Nearby Wi‑Fi Devices permission.
- Some phones require Location Services to be turned on before Wi‑Fi scanning returns results.
