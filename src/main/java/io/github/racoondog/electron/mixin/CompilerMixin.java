package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.ElectronSystem;
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

    @Inject(method = "visitUnary", at = @At("HEAD"), cancellable = true)
    private void optimizeUnaryOperation(Expr.Unary expr, CallbackInfo ci) {
        if (ElectronSystem.get().starscript.get() && StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveUnary(expr));
            ci.cancel();
        }
    }

    @Inject(method = "visitBinary", at = @At("HEAD"), cancellable = true)
    private void optimizeBinaryOperation(Expr.Binary expr, CallbackInfo ci) {
        if (ElectronSystem.get().starscript.get() && StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveBinary(expr));
            ci.cancel();
        }
    }

    @Inject(method = "visitLogical", at = @At("HEAD"), cancellable = true)
    private void optimizeLogicalOperation(Expr.Logical expr, CallbackInfo ci) {
        if (ElectronSystem.get().starscript.get() && StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveLogical(expr));
            ci.cancel();
        }
    }

    @Inject(method = "visitConditional", at = @At("HEAD"), cancellable = true)
    private void optimizeConditionalOperation(Expr.Conditional expr, CallbackInfo ci) {
        if (ElectronSystem.get().starscript.get() && StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveConditional(expr));
            ci.cancel();
        }
    }

    @Inject(method = "visitSection", at = @At("HEAD"), cancellable = true)
    private void ignoreSections(Expr.Section expr, CallbackInfo ci) {
        if (StarscriptUtils.ignoreSections()) {
            compile(expr.expr);
            ci.cancel();
        }
    }
}
