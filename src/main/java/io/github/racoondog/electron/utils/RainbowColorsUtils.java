package io.github.racoondog.electron.utils;

import io.github.racoondog.electron.config.ElectronConfig;
import io.github.racoondog.electron.mixin.tick.colors.IRainbowColors;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.settings.ColorListSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.render.color.RainbowColors;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/**
 * This is unmaintainable
 * @author Crosby
 * @since 0.3.3
 */
@Environment(EnvType.CLIENT)
public final class RainbowColorsUtils {
    private static final List<ColorSetting> rainbowColorSettings = new ArrayList<>();
    private static boolean subscribed = true;

    private static boolean isColorListRainbow(Setting<List<SettingColor>> colorListSetting) {
        if (colorListSetting.module == null || colorListSetting.module.isActive()) {
            for (var settingColor : colorListSetting.get()) if (settingColor.rainbow) return true;
        }
        return false;
    }

    private static boolean areColorListsRainbow() {
        for (var setting : IRainbowColors.getColorListSettings()) {
            if (isColorListRainbow(setting)) return true;
        }
        return false;
    }

    private static void subscribe() {
        if (!subscribed) {
            MeteorClient.EVENT_BUS.subscribe(RainbowColors.class);
            subscribed = true;
        }
    }

    private static void unsubscribe() {
        if (subscribed && !areColorListsRainbow()) {
            MeteorClient.EVENT_BUS.unsubscribe(RainbowColors.class);
            subscribed = false;
        }
    }

    @PostInit(dependencies = RainbowColors.class)
    public static void deactivateByDefault() {
        if (!ElectronConfig.isDisabled("io.github.racoondog.electron.mixin.tick.colors")) {
            subscribe();
            unsubscribe();
        }
    }

    /**
     * Keeps track of which color settings use rainbows and toggle the tick loop accordingly.
     * @author Crosby
     */
    public static void update(ColorSetting colorSetting) {
        if (colorSetting.get().rainbow) {
            if (rainbowColorSettings.isEmpty()) subscribe();
            if (!rainbowColorSettings.contains(colorSetting)) rainbowColorSettings.add(colorSetting);
        } else {
            rainbowColorSettings.remove(colorSetting);
            if (rainbowColorSettings.isEmpty()) unsubscribe();
        }
    }

    public static void update(ColorListSetting colorListSetting) {
        if (!subscribed && isColorListRainbow(colorListSetting)) subscribe();
        else if (rainbowColorSettings.isEmpty()) unsubscribe();
    }

    /**
     * Make sure rainbow colors are actually used.
     * @return true if none of the rainbow colors are in active modules.
     * @author Crosby
     */
    public static boolean shouldSkipTick() {
        if (mc.currentScreen != null && mc.currentScreen.getClass().getPackageName().startsWith("meteor")) return false;

        for (var setting : rainbowColorSettings) {
            if (setting.module != null && setting.module.isActive()) return false;
        }

        return !areColorListsRainbow();
    }
}
