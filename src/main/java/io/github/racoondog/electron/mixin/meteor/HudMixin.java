package io.github.racoondog.electron.mixin.meteor;

import io.github.racoondog.electron.mixininterface.IHud;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.orbit.listeners.ConsumerListener;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Hud.class, remap = false)
public abstract class HudMixin implements IHud {
    @Unique
    private final ConsumerListener<Render2DEvent> render2dCallback = new ConsumerListener<>(
        Render2DEvent.class,
        this::onRender
    );

    @Unique private boolean isSubscribed = false;

    @Shadow public boolean active;
    @Shadow protected abstract void onRender(Render2DEvent event);

    @Override
    public void electron$syncState() {
        if (active) {
            this.isSubscribed = true;
            MeteorClient.EVENT_BUS.subscribe(render2dCallback);
        } else {
            this.isSubscribed = false;
            MeteorClient.EVENT_BUS.unsubscribe(render2dCallback);
        }
    }

    @Inject(method = "fromTag(Lnet/minecraft/nbt/NbtCompound;)Lmeteordevelopment/meteorclient/systems/hud/Hud;", at = @At("RETURN"))
    private void enable(NbtCompound tag, CallbackInfoReturnable<Hud> cir) {
        if (active) {
            this.isSubscribed = true;
            MeteorClient.EVENT_BUS.subscribe(render2dCallback);
        }
    }

    @Inject(method = "lambda$new$1", at = @At("TAIL"))
    private void toggleOnBind(CallbackInfo ci) {
        electron$syncState();
    }

    @Inject(method = "onTick", at = @At("HEAD"))
    private void redundancy(TickEvent.Post event, CallbackInfo ci) {
        if (isSubscribed != active) {
            electron$syncState();
        }
    }
}
