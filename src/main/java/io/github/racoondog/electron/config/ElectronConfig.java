package io.github.racoondog.electron.config;

import io.github.racoondog.electron.Electron;
import io.github.racoondog.electron.utils.Constants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ElectronConfig {
    public static final List<String> SETTINGS = new ArrayList<>();

    static {
        try {
            Path cfg = Constants.CONFIG_FILE;
            //Create if missing
            if (!Files.isRegularFile(cfg)) {
                Constants.LOG.info("Generating config file...");
                try (var writer = Files.newBufferedWriter(cfg, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                    writer.write("//See https://github.com/RacoonDog/Electron for a list of settings.\n");
                    writer.write("io.github.racoondog.electron.mixin.dev.profiler\n");
                    writer.write("io.github.racoondog.electron.mixin.starscript.section\n");
                }
            }
            //Read config
            try (var reader = Files.newBufferedReader(cfg)) {
                String line = reader.readLine();

                while (line != null) {
                    if (!line.startsWith("//") && !line.contains(".required")) {
                        ElectronConfig.SETTINGS.add(line);
                        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Constants.LOG.info(line);
                    }
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            Constants.LOG.error("Could not load config...");
            e.printStackTrace();
        }
    }

    public static boolean isDisabled(String string) {
        for (var setting : SETTINGS) {
            if (string.startsWith(setting)) return true;
        }
        return false;
    }
}
