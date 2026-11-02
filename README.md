# Big Inventory + Tool Rack (Fabric, Minecraft 1.20.1)

NOT COMPILED OR TESTED YET. It was written without access to the Fabric Maven repo,
so expect to fix a few mapping/signature errors on the first build.

## What it does
- Player inventory grows from 36 to 65 slots, drawn 13 wide: 4 storage rows + a 13-slot bottom row.
  Only the first 9 bottom-row slots are the real hotbar (keys 1-9). The other 4 are normal storage.
- Separate 8-slot Tool Rack (right side of the inventory screen). It only accepts tools
  (pickaxe, axe, shovel, hoe, sword, shears, fishing rod, bow, crossbow, trident, flint & steel, brush).
  Items in it do not use inventory or hotbar slots.
- V = equip next tool from rack (swaps with what you hold, so repeated presses cycle).
  B = stow the held tool in the first empty rack slot.
- Rack is saved with the player, kept through respawn/dimension change, dropped on death
  (unless keepInventory).

## Build
1. Install JDK 17+.
2. Easiest: download the Fabric example mod template for 1.20.1, then copy this project's
   `src/`, `build.gradle`, `gradle.properties` and `settings.gradle` over it (keep its gradle wrapper).
   Or with Gradle 8.6+ installed: `gradle wrapper --gradle-version 8.7` then `./gradlew build`.
3. Jar appears in `build/libs/bigtools-0.1.0.jar` (not the `-sources` one).

## Install
Put the jar plus Fabric API (0.92.2+1.20.1) in `mods/` on BOTH the client and the server.

## Known limits / warnings
- Back up the world first. If the mod is removed later, items in slots 37-65 and the tool rack are lost.
- Survival inventory only. Creative inventory is not adapted and will not show the extra slots.
- No recipe book button (the 2x2 crafting grid still works).
- Tool rack is only visible in the inventory screen (no HUD).
- Armor equip sound is skipped when equipping from the new screen.
