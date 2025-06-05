package io.github.racoondog.electron;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.MixinEnvironment;

@Environment(EnvType.CLIENT)
public final class Electron extends MeteorAddon {
    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            MixinEnvironment.getCurrentEnvironment().audit();
        }
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
