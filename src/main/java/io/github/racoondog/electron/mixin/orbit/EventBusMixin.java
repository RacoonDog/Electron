package io.github.racoondog.electron.mixin.orbit;

import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.listeners.IListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = EventBus.class, remap = false)
public class EventBusMixin {
    @Shadow @Final
    private Map<Object, List<IListener>> listenerCache;

    @Inject(method = "unsubscribe(Ljava/lang/Object;)V", at = @At("TAIL"))
    private void invalidateInstanceCache(Object object, CallbackInfo ci) {
        if (!(object instanceof Module) && !(object instanceof System<?>)) {
            listenerCache.remove(object);
        }
    }
}
