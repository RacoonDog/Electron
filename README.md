<div align="center">
  <!-- Logo and Title -->
  <img src="/src/main/resources/assets/electron/icon.png" alt="logo" width="30%"/>
  <h1>Electron</h1>
  <p>A performance optimization addon for Meteor Client which works to speed up launching the game and optimize the tick loop.</p>

  <!-- Fancy badges -->
<a href="https://anticope.ml/pages/MeteorAddons.html"><img src="https://img.shields.io/badge/Verified%20Addon-Yes-yellow" alt="Verified Addon"></a>
<img src="https://img.shields.io/badge/Minecraft%20Version-1.19.2-yellow" alt="Minecraft Version">
<a href="https://github.com/RacoonDog/Electron/commits/main"><img src="https://img.shields.io/github/last-commit/RacoonDog/Electron?logo=github&color=yellow" alt="Last commit"></a>
<img src="https://img.shields.io/github/languages/code-size/RacoonDog/Electron?color=yellow" alt="Code Size">
<img src="https://img.shields.io/github/repo-size/RacoonDog/Electron?color=yellow" alt="Repo Size">
<img src="https://img.shields.io/github/issues/RacoonDog/Electron?color=yellow" alt="Issues">
<img src="https://img.shields.io/github/stars/RacoonDog/Electron?color=yellow" alt="Stars">
<img src="https://img.shields.io/github/downloads/RacoonDog/Electron/total?color=yellow" alt="Downloads">

<a href="https://discord.gg/4RBmBCFSTc"><img src="https://invidget.switchblade.xyz/4RBmBCFSTc"></a>
</div>

# How to use
- Download the latest [release](/../../releases) of the mod from the releases tab.
- Put it in your `.minecraft/mods` folder where you have installed Meteor.

*Note: It is recommended to use the [latest dev build](https://meteorclient.com/download?devBuild=latest) of meteor while using Electron*

## Settings/Optimizations

Put any of these settings in the `meteor-client/electron.txt` file to disable them.
For more advanced configuration, you can also put specific mixin files.
- `io.github.racoondog.electron.mixin.dev`: Enables various developer tools.
- `io.github.racoondog.electron.mixin.dev.profiler`: Enables profiling tools.
- `io.github.racoondog.electron.mixin.loading`: Loading time optimizations.
- `io.github.racoondog.electron.mixin.math`: Math-related optimizations.
- `io.github.racoondog.electron.mixin.math.fma`: Uses optimized FMA instructions in place of some mathematical operations.
- `io.github.racoondog.electron.mixin.render`: Rendering optimizations.
- `io.github.racoondog.electron.mixin.render.culling`: Render culling optimizations.
- `io.github.racoondog.electron.mixin.starscript`: Starscript optimizations.
- `io.github.racoondog.electron.mixin.starscript.section`: Disables section instructions. Only use if you know what you are doing.
- `io.github.racoondog.electron.mixin.starscript.raw`: Replaces starscript implementation with formatting-less versions in some cases.
- `io.github.racoondog.electron.mixin.stream`: Replaces stream operations with iterative implementations when the list size is small.
- `io.github.racoondog.electron.mixin.tick`: Tick loop optimizations.
- `io.github.racoondog.electron.mixin.tick.colors`: Disables rainbow color updates when not in use.
- `io.github.racoondog.electron.mixin.tick.blockiterator`: Replaces the BlockIterator with an optimized version.
