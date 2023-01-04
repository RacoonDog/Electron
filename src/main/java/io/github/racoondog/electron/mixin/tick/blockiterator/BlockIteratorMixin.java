package io.github.racoondog.electron.mixin.tick.blockiterator;

import io.github.racoondog.electron.utils.ChunkBlockIterator;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
@Mixin(value = BlockIterator.class, remap = false)
public abstract class BlockIteratorMixin {
    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void removeTick(TickEvent.Pre event, CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "register", at = @At("HEAD"))
    private static void replaceRegister(int horizontalRadius, int verticalRadius, BiConsumer<BlockPos, BlockState> function, CallbackInfo ci) {
        ChunkBlockIterator.register(horizontalRadius, verticalRadius, function);
    }

    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "disableCurrent", at = @At("HEAD"), cancellable = true)
    private static void replaceDisableCurrent(CallbackInfo ci) {
        ChunkBlockIterator.disableCurrent();
        ci.cancel();
    }

    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "after", at = @At("HEAD"), cancellable = true)
    private static void replaceAfter(Runnable callback, CallbackInfo ci) {
        ChunkBlockIterator.after(callback);
        ci.cancel();
    }
}
