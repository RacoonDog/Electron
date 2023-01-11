package io.github.racoondog.electron.mixin.starscript;

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
     * Tries to solve Unary operations.
     *
     * @author Crosby
     */
    @Inject(method = "visitUnary", at = @At("HEAD"), cancellable = true)
    private void optimizeUnaryOperation(Expr.Unary expr, CallbackInfo ci) {
        if (StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveUnary(expr));
            ci.cancel();
        }
    }

    /**
     * Tries to solve Binary operations.
     *
     * @author Crosby
     */
    @Inject(method = "visitBinary", at = @At("HEAD"), cancellable = true)
    private void optimizeBinaryOperation(Expr.Binary expr, CallbackInfo ci) {
        if (StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveBinary(expr));
            ci.cancel();
        }
    }

    /**
     * Tries to solve Logical operations.
     *
     * @author Crosby
     */
    @Inject(method = "visitLogical", at = @At("HEAD"), cancellable = true)
    private void optimizeLogicalOperation(Expr.Logical expr, CallbackInfo ci) {
        if (StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveLogical(expr));
            ci.cancel();
        }
    }

    /**
     * Tries to solve Conditional operations.
     *
     * @author Crosby
     */
    @Inject(method = "visitConditional", at = @At("HEAD"), cancellable = true)
    private void optimizeConditionalOperation(Expr.Conditional expr, CallbackInfo ci) {
        if (StarscriptUtils.isSolveable(expr)) {
            compile(StarscriptUtils.solveConditional(expr));
            ci.cancel();
        }
    }
}
