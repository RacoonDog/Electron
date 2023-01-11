package io.github.racoondog.electron.mixin.tick.colors;

import io.github.racoondog.electron.utils.RainbowColorsUtils;
import meteordevelopment.meteorclient.settings.ColorListSetting;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ColorListSetting.class)
public abstract class ColorListSettingMixin extends Setting<List<SettingColor>> {
    private ColorListSettingMixin(String name, String description, List<SettingColor> defaultValue, Consumer<List<SettingColor>> onChanged, Consumer<Setting<List<SettingColor>>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        RainbowColorsUtils.update((ColorListSetting) (Object) this);
    }
}
