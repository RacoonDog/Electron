package io.github.racoondog.electron.mixin.tick.colors;

import io.github.racoondog.electron.mixininterface.IColorSetting;
import io.github.racoondog.electron.utils.RainbowColorsUtils;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(value = ColorSetting.class, remap = false)
public abstract class ColorSettingMixin extends Setting<SettingColor> implements IColorSetting {
    @Unique private boolean doNotUpdate = false;

    private ColorSettingMixin(String name, String description, SettingColor defaultValue, Consumer<SettingColor> onChanged, Consumer<Setting<SettingColor>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        if (!this.doNotUpdate) RainbowColorsUtils.update((ColorSetting) (Object) this);
    }

    @Override
    public void setDoNotUpdate(boolean newValue) {
        this.doNotUpdate = newValue;
    }

    @Override
    public boolean getDoNotUpdate() {
        return this.doNotUpdate;
    }
}
