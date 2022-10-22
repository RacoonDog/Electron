package io.github.racoondog.electron.mixin;

import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(value = Systems.class, remap = false)
public interface ISystems {
    @SuppressWarnings("UnusedReturnValue")
    @Invoker
    static System<?> invokeAdd(System<?> system) {
        throw new AssertionError();
    }
}
