package io.github.racoondog.electron.config;

import io.github.racoondog.electron.Electron;
import meteordevelopment.meteorclient.MeteorClient;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ElectronConfig implements PreLaunchEntrypoint {
    public static final List<String> SETTINGS = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onPreLaunch() {
        try {
            File file = new File(MeteorClient.FOLDER, "electron.txt");
            //Create if missing
            if (!file.exists()) {
                Electron.LOG.info("Generating config file...");
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
            Electron.LOG.error("Could not load config...");
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
