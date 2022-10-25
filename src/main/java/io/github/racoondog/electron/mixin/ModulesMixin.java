package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.utils.ThreadUtils;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Modules.class, remap = false)
public abstract class ModulesMixin {
    /**
     * Ensures thread-safety.
     *
     * @author Crosby
     */
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void addModuleToQueue(Module module, CallbackInfo ci) {
        if (ThreadUtils.initLock) {
            ThreadUtils.MODULE_QUEUE.add(module);
            ci.cancel();
        }
    }
}
