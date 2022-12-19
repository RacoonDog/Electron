package io.github.racoondog.electron.mixin.mixin.starscript;

import meteordevelopment.meteorclient.systems.hud.elements.TextHud;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.Section;
import meteordevelopment.starscript.Starscript;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = TextHud.class, remap = false)
public abstract class TextHudMixin {
    @Unique private final StringBuilder stringBuilder = new StringBuilder();

    /**
     * Reuse {@link StringBuilder} objects.
     *
     * @author Crosby
     * @since 0.2.4
     */
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/starscript/Starscript;run(Lmeteordevelopment/starscript/Script;)Lmeteordevelopment/starscript/Section;"))
    private Section useStringBuilder1(Starscript instance, Script script) {
        return instance.run(script, stringBuilder);
    }

    /**
     * Reuse {@link StringBuilder} objects.
     *
     * @author Crosby
     * @since 0.2.4
     */
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/misc/MeteorStarscript;run(Lmeteordevelopment/starscript/Script;)Ljava/lang/String;"))
    private String useStringBuilder2(Script script) {
        return MeteorStarscript.run(script, stringBuilder);
    }
}
