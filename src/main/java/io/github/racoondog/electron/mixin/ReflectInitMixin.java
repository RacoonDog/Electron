package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.Electron;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import meteordevelopment.meteorclient.utils.ReflectInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.Annotation;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(value = ReflectInit.class, remap = false)
public abstract class ReflectInitMixin {
    @Unique private static final Map<String, Reflections> REFLECTIONS_CACHE = new Object2ObjectOpenHashMap<>();

    /**
     * Caches {@link Reflections} instances
     *
     * @author Crosby
     */
    @Redirect(method = "init", at = @At(value = "NEW", target = "org/reflections/Reflections"))
    private static Reflections cacheReflections(String prefix, Scanner[] scanners) {
        if (REFLECTIONS_CACHE.containsKey(prefix)) return REFLECTIONS_CACHE.get(prefix);
        Reflections reflections = new Reflections(prefix, scanners);
        REFLECTIONS_CACHE.put(prefix, reflections);
        return reflections;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private static void inject(Class<? extends Annotation> annotation, CallbackInfo ci) {
        Electron.LOG.debug("Unique ReflectInit package names: {}", REFLECTIONS_CACHE.size());
    }
}
