package io.github.racoondog.electron.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Mixin safe objects
 */
@Environment(EnvType.CLIENT)
public final class Constants {
    public static final Path CONFIG_FILE = FabricLoader.getInstance().getGameDir().resolve("meteor-client").resolve("electron.txt");
    public static final Logger LOG = LoggerFactory.getLogger("Electron");
}
