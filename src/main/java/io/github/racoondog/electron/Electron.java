package io.github.racoondog.electron;

import com.mojang.logging.LogUtils;
import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;

import java.io.*;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public final class Electron extends MeteorAddon implements PreLaunchEntrypoint {
    public static final Logger LOG = LogUtils.getLogger();

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "ConstantConditions"})
    @Override
    public void onInitialize() {
        final String versionString = FabricLoader.getInstance().getModContainer("electron").get().getMetadata().getVersion().getFriendlyString();
        TitleScreenCredits.registerCustomDrawFunction(this, (matrices, credit, y) -> {
            int width1 = mc.textRenderer.getWidth("Electron ");
            int width2 = mc.textRenderer.getWidth("(");
            int width3 = mc.textRenderer.getWidth(versionString);
            int width4 = mc.textRenderer.getWidth(") by ");
            int widthName = mc.textRenderer.getWidth("Crosby");

            int x = mc.currentScreen.width - 3 - width1 - width2 - width3 - width4 - widthName;

            mc.textRenderer.drawWithShadow(matrices, "Electron ", x, y, Color.fromRGBA(255, 201, 58, 255));
            mc.textRenderer.drawWithShadow(matrices, "(", x + width1, y, TitleScreenCredits.GRAY);
            mc.textRenderer.drawWithShadow(matrices, versionString, x + width1 + width2, y, TitleScreenCredits.WHITE);
            mc.textRenderer.drawWithShadow(matrices, ") by ", x + width1 + width2 + width3, y, TitleScreenCredits.GRAY);
            mc.textRenderer.drawWithShadow(matrices, "Crosby", x + width1 + width2 + width3 + width4, y, TitleScreenCredits.WHITE);
        }, true);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onPreLaunch() {
        try {
            File file = new File(MeteorClient.FOLDER, "electron.txt");
            //Create if missing
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (var writer = new FileWriter(file)) {
                    writer.write("//See https://github.com/RacoonDog/Electron for a list of settings.");
                    writer.write("io.github.racoondog.electron.mixin.dev.profiler");
                    writer.write("io.github.racoondog.electron.mixin.starscript.section");
                }
            }
            //Read config
            try (var reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();

                while (line != null) {
                    if (!line.startsWith("//")) ElectronMixinPlugin.SETTINGS.add(line);
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
