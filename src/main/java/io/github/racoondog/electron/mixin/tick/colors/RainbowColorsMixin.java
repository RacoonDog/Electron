package io.github.racoondog.electron.mixin.tick.colors;

import io.github.racoondog.electron.utils.RainbowColorsUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.render.color.RainbowColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = RainbowColors.class, remap = false)
public abstract class RainbowColorsMixin {
    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void onTick(TickEvent.Post event, CallbackInfo ci) {
        if (RainbowColorsUtils.shouldSkipTick()) ci.cancel();
    }
}
