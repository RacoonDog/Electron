package io.github.racoondog.electron.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import io.github.racoondog.electron.WeakLazy;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.NoRender;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.font.TextRenderer$Drawer")
public class TextRenderer$DrawerMixin {
    @Unique
    private final WeakLazy<NoRender> noRender = new WeakLazy<>(() -> Modules.get() != null ? Modules.get().get(NoRender.class) : null);

    @Unique @Nullable
    private NoRender getNoRender() {
        return noRender.get();
    }

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidMemberReference"})
    @TargetHandler(
        mixin = "meteordevelopment.meteorclient.mixin.TextRendererMixin",
        name = "onRenderObfuscatedStyle"
    )
    @Inject(
        method = "@MixinSquared:Handler",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cacheModuleInstance(boolean original, CallbackInfoReturnable<Boolean> cir) {
        NoRender module = getNoRender();
        if (module == null) {
            cir.setReturnValue(original);
        } else {
            cir.setReturnValue(!module.noObfuscation() && original);
        }
    }
}
