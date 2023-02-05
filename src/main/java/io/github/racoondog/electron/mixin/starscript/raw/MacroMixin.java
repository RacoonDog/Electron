package io.github.racoondog.electron.mixin.starscript.raw;

import io.github.racoondog.electron.utils.StarscriptUtils;
import meteordevelopment.meteorclient.systems.macros.Macro;
import meteordevelopment.starscript.Script;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = Macro.class, remap = false)
public abstract class MacroMixin {
    @Unique private final StringBuilder stringBuilder = new StringBuilder();

    /**
     * Reuse {@link StringBuilder} objects.
     *
     * @author Crosby
     * @since 0.2.4
     */
    @Redirect(method = "onAction()Z", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/misc/MeteorStarscript;run(Lmeteordevelopment/starscript/Script;)Ljava/lang/String;"))
    private String useStringBuilder(Script script) {
        return StarscriptUtils.runRawString(script, stringBuilder);
    }
}
