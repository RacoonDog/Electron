package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.utils.ThreadUtils;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Tabs.class, remap = false)
public abstract class TabsMixin {
    /**
     * Ensures thread-safety.
     *
     * @author Crosby
     */
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private static void addTabToQueue(Tab tab, CallbackInfo ci) {
        if (ThreadUtils.initLock) {
            ThreadUtils.TAB_QUEUE.add(tab);
            ci.cancel();
        }
    }
}
