package io.github.racoondog.electron.mixin.tick.colors;

import io.github.racoondog.electron.mixininterface.IColorSetting;
import meteordevelopment.meteorclient.gui.DefaultSettingsWidgetFactory;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = DefaultSettingsWidgetFactory.class, remap = false)
public abstract class DefaultSettingsWidgetFactoryMixin {
    @Redirect(method = "lambda$colorListWFill$74", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/settings/ColorSetting;set(Ljava/lang/Object;)Z"))
    private boolean updateRainbowColorsUtils(ColorSetting instance, Object o) {
        ((IColorSetting) instance).setDoNotUpdate(true);
        return instance.set((SettingColor) o);
    }
}
