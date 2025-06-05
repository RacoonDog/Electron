package io.github.racoondog.electron.mixin.meteor;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.orbit.listeners.IListener;
import net.lenni0451.reflect.stream.RStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = Systems.class, remap = false)
public class SystemsMixin {
    @Inject(method = "add", at = @At("RETURN"))
    private static void hudCallback(System<?> system, CallbackInfoReturnable<System<?>> cir) {
        if (system instanceof Hud hud) {
            // Modify cache to hide render event
            MeteorClient.EVENT_BUS.unsubscribe(hud);

            Map<Object, List<IListener>> listenerCache = RStream.of(MeteorClient.EVENT_BUS).fields()
                .by("listenerCache").get();

            listenerCache.get(hud).removeIf(listener -> listener.getTarget() == Render2DEvent.class);

            // Resubscribe other event handlers
            MeteorClient.EVENT_BUS.subscribe(hud);
        }
    }
}
