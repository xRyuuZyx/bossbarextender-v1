# BossBar Extender (Fabric client mod)

**Target Minecraft Java versions:** 1.21.1 through 1.21.11 inclusive.

This is a CLIENT-SIDE Fabric mod. Install it on each player's client (PC, laptop, or Android Java launcher/Pojav-compatible Fabric instance). It does not go in the Paper server's `plugins` folder and it does not require a server plugin.

## What it changes

- Replaces only the vanilla BossBar HUD render loop.
- Uses Minecraft's own BossBar drawing routine and text renderer, so normal BossBar textures, colors, notched styles, progress, and title rendering are retained.
- Removes the vanilla early cutoff at roughly one-third of the scaled screen height.
- Renders up to a configurable number of BossBars, while respecting a configurable percentage of the screen height.
- Does not edit UIStudio, its resource pack, or other HUD elements.

## Installation

1. Install Fabric Loader for the exact Minecraft version being launched.
2. Put `bossbar-extender-1.0.0.jar` in that client's `mods` folder.
3. Launch the game once. The config is created at `.minecraft/config/bossbar-extender.properties`.
4. Repeat on each PC, laptop, and phone client that needs the expanded BossBar display.

No Fabric API is required by this mod. Fabric Loader is required.

## Configuration

```properties
max-boss-bars=32
# Same vanilla position at the top of the screen.
top-offset=12
# Vanilla BossBar row spacing. Keep 19 for the original appearance.
bar-spacing=19
# Maximum vertical area used by the stack. Lower this on small displays if needed.
max-screen-usage-percent=85
```

After changing settings, restart Minecraft. The mod does not shrink bars or change their textures; it only allows more rows to be drawn within the available screen area.

## Compatibility notes

The mod is built against Minecraft 1.21.11 using Yarn mappings. Its Mixin targets the intermediary BossBarHud class and the same intermediary render/field identifiers documented in the 1.21.1, 1.21.2, and 1.21.11 mappings. Fabric's intermediary namespace is intended to keep these identifiers stable across obfuscated releases. The metadata declares the full supported range `>=1.21.1 <=1.21.11`.

The project has not yet been run through a live Minecraft client test matrix for every one of the 11 patch releases. Test on a backup profile first. If an individual version changes the target method, that version may need a compatibility patch.

## Build

Requires JDK 21 and Gradle 9.2.1. Run:

```bash
gradle build
```

The GitHub Actions workflow in `.github/workflows/build.yml` builds the remapped mod JAR and publishes it as a workflow artifact.
