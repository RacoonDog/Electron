package io.github.racoondog.electron.mixin.dev.profiler;

import com.google.common.collect.Lists;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.listeners.LambdaListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(value = LambdaListener.class, remap = false)
public abstract class LambdaListenerMixin {
    @Unique private static final List<Class<?>> ALLOWED_EVENTS = Lists.newArrayList(TickEvent.Pre.class, TickEvent.Post.class, Render2DEvent.class, Render3DEvent.class);

    @Shadow @Final private Class<?> target;

    @Unique private String identifier;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void saveId(LambdaListener.Factory factory, Class klass, Object object, Method method, CallbackInfo ci) {
        if (ALLOWED_EVENTS.contains(target)) identifier = MeteorClient.MOD_ID + "_" + method.getDeclaringClass().getSimpleName() + "_" + method.getName();
    }

    @Inject(method = "call", at = @At("HEAD"))
    private void push(Object event, CallbackInfo ci) {
        if (identifier != null) mc.getProfiler().push(identifier);
    }

    @Inject(method = "call", at = @At("TAIL"))
    private void pop(Object event, CallbackInfo ci) {
        if (identifier != null) mc.getProfiler().pop();
    }
}
