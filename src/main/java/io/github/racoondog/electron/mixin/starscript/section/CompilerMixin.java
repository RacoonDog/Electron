package io.github.racoondog.electron.mixin.starscript.section;

import io.github.racoondog.electron.utils.StarscriptUtils;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Expr;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Compiler.class, remap = false)
public abstract class CompilerMixin {
    @Shadow protected abstract void compile(Expr expr);

    /**
     * Removes Sections if applicable.
     *
     * @author Crosby
     */
    @Inject(method = "visitSection", at = @At("HEAD"), cancellable = true)
    private void ignoreSections(Expr.Section expr, CallbackInfo ci) {
        if (StarscriptUtils.IGNORE_SECTIONS) {
            compile(expr.expr);
            ci.cancel();
        }
    }
}
