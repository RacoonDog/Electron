package io.github.racoondog.electron.mixin.mixin.asyncaddoninit;

import io.github.racoondog.electron.utils.ThreadUtils;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = Systems.class, remap = false)
public abstract class SystemsMixin {
    /**
     * Ensures thread-safety.
     *
     * @author Crosby
     */
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private static void addSystemToQueue(System<?> system, CallbackInfoReturnable<System<?>> cir) {
        if (ThreadUtils.initLock) {
            ThreadUtils.SYSTEM_QUEUE.add(system);
            cir.setReturnValue(system);
            cir.cancel();
        }
    }
}
