package io.github.racoondog.electron.mixin.rainbowcolors;

import io.github.racoondog.electron.utils.RainbowColorsUtils;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ColorSetting.class)
public abstract class ColorSettingMixin extends Setting<SettingColor> {
    public ColorSettingMixin(String name, String description, SettingColor defaultValue, Consumer<SettingColor> onChanged, Consumer<Setting<SettingColor>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        RainbowColorsUtils.update((ColorSetting) (Object) this);
    }
}
