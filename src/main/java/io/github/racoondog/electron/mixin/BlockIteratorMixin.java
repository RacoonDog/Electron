package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.ElectronSystem;
import io.github.racoondog.electron.blockiterator.ChunkBlockIterator;
import io.github.racoondog.electron.dev.BlockIterators;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.widgets.WLabel;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
@Mixin(value = BlockIterator.class, remap = false)
public abstract class BlockIteratorMixin {
    @Shadow @Final private static List<Runnable> afterCallbacks;
    @Unique private static long startTime;

    /**
     * Disables old BlockIterator and enables dev benchmarking.
     *
     * @author Crosby
     */
    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void startTimer(TickEvent.Pre event, CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) ci.cancel();
        else if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            if (afterCallbacks.isEmpty()) ci.cancel();
            startTime = System.nanoTime();
        }
    }

    /**
     * Enables dev benchmarking.
     *
     * @author Crosby
     */
    @Inject(method = "onTick", at = @At("TAIL"))
    private static void endTimer(TickEvent.Pre event, CallbackInfo ci) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            WLabel label = Modules.get().get(BlockIterators.class).bITime;
            if (label != null) label.set("%s microseconds".formatted((System.nanoTime() - startTime) / 1000));
        }
    }

    /**
     * Disables old BlockIterator.
     *
     * @author Crosby
     */
    @Inject(method = "register", at = @At("HEAD"))
    private static void replaceRegister(int horizontalRadius, int verticalRadius, BiConsumer<BlockPos, BlockState> function, CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) ChunkBlockIterator.register(horizontalRadius, verticalRadius, function);
    }

    /**
     * Disables old BlockIterator.
     *
     * @author Crosby
     */
    @Inject(method = "disableCurrent", at = @At("HEAD"))
    private static void replaceDisableCurrent(CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) ChunkBlockIterator.disableCurrent();
    }

    /**
     * Disables old BlockIterator.
     *
     * @author Crosby
     */
    @Inject(method = "after", at = @At("HEAD"))
    private static void replaceAfter(Runnable callback, CallbackInfo ci) {
        if (ElectronSystem.get().chunkBlockIterator.get()) ChunkBlockIterator.after(callback);
    }
}
