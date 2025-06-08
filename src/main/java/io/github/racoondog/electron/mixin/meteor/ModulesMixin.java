package io.github.racoondog.electron.mixin.meteor;

import io.github.racoondog.electron.WeakLazy;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Modules.class, remap = false)
public class ModulesMixin {
    @Unique
    private static final WeakLazy<Modules> modules = new WeakLazy<>(() -> Systems.get(Modules.class));

    /**
     * @author Crosby
     * @reason speed up hot path
     */
    @Overwrite
    public static Modules get() {
        return modules.get();
    }
}
