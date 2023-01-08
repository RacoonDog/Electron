package io.github.racoondog.electron.mixin.render.culling;

import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.render.LightOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(value = LightOverlay.class, remap = false)
public abstract class LightOverlayMixin {
    @Shadow @Final private Setting<Boolean> seeThroughBlocks;

    /**
     * Don't render crosses if they are higher than the camera's Y position.
     *
     * @author Crosby
     * @since 0.2.5
     */
    @Inject(method = "lambda$onTick$1", at = @At("HEAD"), cancellable = true)
    private void cullIfHidden(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (!seeThroughBlocks.get() && blockPos.getY() > mc.gameRenderer.getCamera().getBlockPos().getY()) ci.cancel();
    }
}
