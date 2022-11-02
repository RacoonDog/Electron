package io.github.racoondog.electron.mixin.asyncaddoninit;

import io.github.racoondog.electron.utils.ThreadUtils;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Hud.class, remap = false)
public abstract class HudMixin {
    /**
     * Ensures thread-safety.
     *
     * @author Crosby
     */
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private void addElementToQueue(HudElementInfo<?> info, CallbackInfo ci) {
        if (ThreadUtils.initLock) {
            ThreadUtils.HEI_QUEUE.add(info);
            ci.cancel();
        }
    }
}
