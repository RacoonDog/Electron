package io.github.racoondog.electron.mixin.mixin;

import io.github.racoondog.electron.ElectronSystem;
import io.github.racoondog.electron.blockiterator.ChunkBlockIterator;
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
        if (ElectronSystem.get().chunkBlockIterator.get()) ci.cancel();
    }

    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "register", at = @At("HEAD"))
    private static void replaceRegister(int horizontalRadius, int verticalRadius, BiConsumer<BlockPos, BlockState> function, CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) ChunkBlockIterator.register(horizontalRadius, verticalRadius, function);
    }

    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "disableCurrent", at = @At("HEAD"), cancellable = true)
    private static void replaceDisableCurrent(CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) {
            ChunkBlockIterator.disableCurrent();
            ci.cancel();
        }
    }

    /**
     * Disables old {@link BlockIterator}.
     *
     * @author Crosby
     */
    @Inject(method = "after", at = @At("HEAD"), cancellable = true)
    private static void replaceAfter(Runnable callback, CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) {
            ChunkBlockIterator.after(callback);
            ci.cancel();
        }
    }
}
