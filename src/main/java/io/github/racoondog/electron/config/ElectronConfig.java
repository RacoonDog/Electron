package io.github.racoondog.electron.config;

import io.github.racoondog.electron.utils.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ElectronConfig {
    public static final List<String> SETTINGS = new ArrayList<>();

    static {
        try {
            File file = Constants.CONFIG_FILE;
            //Create if missing
            if (!file.exists()) {
                Constants.LOG.info("Generating config file...");
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (var writer = new FileWriter(file)) {
                    writer.write("//See https://github.com/RacoonDog/Electron for a list of settings.\n");
                    writer.write("io.github.racoondog.electron.mixin.dev.profiler\n");
                    writer.write("io.github.racoondog.electron.mixin.starscript.section\n");
                }
            }
            //Read io.github.racoondog.electron.config
            try (var reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();

                while (line != null) {
                    if (!line.startsWith("//") && !line.contains(".required")) ElectronConfig.SETTINGS.add(line);
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
