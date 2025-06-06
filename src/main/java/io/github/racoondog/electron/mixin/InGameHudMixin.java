package io.github.racoondog.electron.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import io.github.racoondog.electron.orbit.Invokers;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.orbit.IEventBus;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 2000)
public class InGameHudMixin {
    /**
     * @author Crosby
     * @reason Skip 2D rendering setup if unused
     */
    @SuppressWarnings({"MixinAnnotationTarget", "InvalidMemberReference", "CancellableInjectionUsage"})
    @TargetHandler(
        mixin = "meteordevelopment.meteorclient.mixin.InGameHudMixin",
        name = "onRender"
    )
    @Inject(
        method = "@MixinSquared:Handler",
        at = @At("HEAD"),
        cancellable = true
    )
    private void skipSetupIfUnused(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci1, CallbackInfo ci2) {
        if (!Invokers.RENDER_2D_INVOKER.isListening()) {
            ci2.cancel();
        }
    }

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidMemberReference", "UnresolvedMixinReference"})
    @TargetHandler(
        mixin = "meteordevelopment.meteorclient.mixin.InGameHudMixin",
        name = "onRender"
    )
    @Redirect(
        method = "@MixinSquared:Handler",
        at = @At(value = "INVOKE", target = "Lmeteordevelopment/orbit/IEventBus;post(Ljava/lang/Object;)Ljava/lang/Object;")
    )
    private Object skipSetupIfUnused(IEventBus bus, Object event) {
        return Invokers.RENDER_2D_INVOKER.post((Render2DEvent) event);
    }
}
