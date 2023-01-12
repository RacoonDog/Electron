package io.github.racoondog.electron.mixin.tick.colors;

import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.render.color.RainbowColors;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = RainbowColors.class, remap = false)
public interface IRainbowColors {
    @Accessor
    static List<Setting<List<SettingColor>>> getColorListSettings() {
        throw new AssertionError();
    }
}
