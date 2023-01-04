package io.github.racoondog.electron.utils;

import io.github.racoondog.electron.config.ElectronConfig;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.render.color.RainbowColors;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/** Note: This system might break when using {@link meteordevelopment.meteorclient.settings.ColorListSetting} */
public final class RainbowColorsUtils {
    private static final List<ColorSetting> rainbowColorSettings = new ArrayList<>();

    @PostInit(dependencies = RainbowColors.class)
    public static void deactivateByDefault() {
        if (!ElectronConfig.isDisabled("io.github.racoondog.electron.mixin.tick.colors")) MeteorClient.EVENT_BUS.unsubscribe(RainbowColors.class);
    }

    /**
     * Keeps track of which color settings use rainbows and toggle the tick loop accordingly.
     * @author Crosby
     */
    public static void update(ColorSetting colorSetting) {
        if (colorSetting.get().rainbow) {
            if (rainbowColorSettings.isEmpty()) MeteorClient.EVENT_BUS.subscribe(RainbowColors.class);
            rainbowColorSettings.add(colorSetting);
        } else {
            rainbowColorSettings.remove(colorSetting);
            if (rainbowColorSettings.isEmpty()) MeteorClient.EVENT_BUS.unsubscribe(RainbowColors.class);
        }
    }

    /**
     * Make sure rainbow colors are actually used.
     * @return true if none of the rainbow colors are in active modules.
     * @author Crosby
     */
    public static boolean shouldSkipTick() {
        if (mc.currentScreen.getClass().getPackageName().startsWith("meteor")) return false;

        for (var setting : rainbowColorSettings) {
            if (setting.module != null && setting.module.isActive()) return false;
        }
        return true;
    }
}
