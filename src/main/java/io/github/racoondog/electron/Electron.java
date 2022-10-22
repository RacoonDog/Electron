package io.github.racoondog.electron;

import com.mojang.logging.LogUtils;
import io.github.racoondog.electron.dev.BlockIterators;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public final class Electron extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Electron");

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Modules.get().add( new BlockIterators() );
        }
    }

    @Override
    public void onRegisterCategories() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.electron";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "Electron");
    }
}
