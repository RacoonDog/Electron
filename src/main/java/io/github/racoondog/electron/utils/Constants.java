package io.github.racoondog.electron.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Mixin safe objects
 */
@Environment(EnvType.CLIENT)
public final class Constants {
    public static final File CONFIG_FILE = new File(new File(FabricLoader.getInstance().getGameDir().toString(), "meteor-client"), "electron.txt"); //Cannot use MeteorClient.FOLDER as it is not mixin safe
    public static final Logger LOG = LoggerFactory.getLogger("Electron");
}
