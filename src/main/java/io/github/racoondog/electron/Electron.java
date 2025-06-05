package io.github.racoondog.electron;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class Electron extends MeteorAddon {
    @Override
    public void onInitialize() {
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.electron";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "Electron", "main", null);
    }
}
