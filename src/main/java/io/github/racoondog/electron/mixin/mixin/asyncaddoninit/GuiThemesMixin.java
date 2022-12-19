package io.github.racoondog.electron.mixin.mixin.asyncaddoninit;

import io.github.racoondog.electron.utils.ThreadUtils;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = GuiThemes.class, remap = false)
public abstract class GuiThemesMixin {
    /**
     * Ensures thread-safety.
     *
     * @author Crosby
     */
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private static void addThemeToQueue(GuiTheme theme, CallbackInfo ci) {
        if (ThreadUtils.initLock) {
            ThreadUtils.THEME_QUEUE.add(theme);
            ci.cancel();
        }
    }
}
