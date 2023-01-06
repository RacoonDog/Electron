package io.github.racoondog.electron;

import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public final class Electron extends MeteorAddon {
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void onInitialize() {
        final String versionString = FabricLoader.getInstance().getModContainer("electron").get().getMetadata().getVersion().getFriendlyString();
        TitleScreenCredits.modifyAddonCredit(this, credit -> {
            credit.sections.add(1, new TitleScreenCredits.Section(" (", TitleScreenCredits.GRAY));
            credit.sections.add(2, new TitleScreenCredits.Section(versionString, TitleScreenCredits.WHITE));
            credit.sections.add(3, new TitleScreenCredits.Section(")", TitleScreenCredits.GRAY));
        });
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
