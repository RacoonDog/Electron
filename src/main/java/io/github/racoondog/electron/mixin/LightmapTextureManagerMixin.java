package io.github.racoondog.electron.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.Fullbright;
import meteordevelopment.meteorclient.systems.modules.render.Xray;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Unique
    private boolean modifiedLightmap = false;

    /**
     * Note: {@code return} means pass-through and update, {@code cancel} means prevent update
     *
     * @author Crosby
     * @reason Prevent redundant lightmap updates if gamma is overridden to max
     */
    @SuppressWarnings({"ConstantValue", "UnnecessaryReturnStatement"})
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void inject(float tickProgress, CallbackInfo ci) {
        boolean shouldFullbright = Modules.get().get(Fullbright.class).getGamma() || Modules.get().isActive(Xray.class);

        if (shouldFullbright && !modifiedLightmap) {
            modifiedLightmap = true;
            return;
        }

        if (shouldFullbright && modifiedLightmap) {
            ci.cancel();
            return;
        }

        if (!shouldFullbright && modifiedLightmap) {
            modifiedLightmap = false;
            return;
        }
    }
}
