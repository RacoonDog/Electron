<div align="center">
  <!-- Logo and Title -->
  <img src="/src/main/resources/assets/electron/icon.png" alt="logo" width="30%"/>
  <h1>Electron</h1>
  <p>A performance optimization addon for Meteor Client which works to speed up launching the game and optimize the tick loop.</p>

  <!-- Fancy badges -->
<a href="https://anticope.ml/pages/MeteorAddons.html"><img src="https://img.shields.io/badge/Verified%20Addon-Yes-blueviolet" alt="Verified Addon"></a>
<img src="https://img.shields.io/badge/Minecraft%20Version-1.19.2-blue" alt="Minecraft Version">
<a href="https://github.com/RacoonDog/Electron/commits/main"><img src="https://img.shields.io/github/last-commit/RacoonDog/Electron?logo=git" alt="Last commit"></a>
<img src="https://img.shields.io/github/languages/code-size/RacoonDog/Electron" alt="Code Size">
<img src="https://img.shields.io/github/repo-size/RacoonDog/Electron" alt="Repo Size">
<img src="https://img.shields.io/github/issues/RacoonDog/Electron" alt="Issues">
<img src="https://img.shields.io/github/stars/RacoonDog/Electron" alt="Stars">
<img src="https://img.shields.io/github/downloads/RacoonDog/Electron/total" alt="Downloads">
</div>

# How to use
- Download the latest [release](/../../releases) of the mod from the releases tab.
- Put it in your `.minecraft/mods` folder where you have installed Meteor.

*Note: It is recommended to use the [latest dev build](https://meteorclient.com/download?devBuild=latest) of meteor while using Electron*

## Settings

- `Chunk Block Iterator` : Optimizes the block iterator used in modules such as HoleFiller, LightOverlay and Nuker. This has the side effect of changing the order of blocks returned, which should not change normal Meteor Client behavior.
- `Async Addon Init` : Initializes multiple addons at the same time. Reduces game launch time by ~100-200 ms with 5 addons and 4 threads. 
    - `Addon Init Threads` : Changes the amount of threads allocated to asynchronous addon initialization.
- `Starscript Optimizations` : Enables Starscript optimizations. This also enables static constant folding and propagation.
    - `Null On Error` : Makes the Starscript Optimizer replace an erroring instruction with `null` instead of skipping it.
    - `Ignore Sections` : Makes the Starscript Optimizer delete section instructions. Only use if you know what you are doing.
    - `Null Propagation` : Makes the Starscript Optimizer replace functions which have `null` parameters with `null` instructions. This is only really useful if `Null On Error` is activated. This has the side effect of breaking functions which take `null` as parameters.

## Other Optimizations

- `ReflectInit Caching` : Reduces game launch time by ~100-300 ms with 5 addons.
- `Rainbow` : Deactivates the `RainbowColors` tick loop when no rainbow colors are in use.
- `Starscript` : Ignores `Section` instructions when context does not allow colored text and re-use `StringBuilder` objects when applicable.
- `Light Overlay` : Skips rendering crosses that are above the camera's Y position.
- `Hole Filler` : Stops iterating blocks when past the blocks per tick threshold.

& Various utility method optimizations.

*totally didnt steal this readme template*
