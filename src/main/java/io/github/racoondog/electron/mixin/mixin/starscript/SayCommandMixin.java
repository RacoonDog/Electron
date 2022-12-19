package io.github.racoondog.electron.mixin.mixin.starscript;

import meteordevelopment.meteorclient.systems.commands.commands.SayCommand;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Script;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = SayCommand.class, remap = false)
public abstract class SayCommandMixin {
    @Unique private static final StringBuilder stringBuilder = new StringBuilder();

    /**
     * Reuse {@link StringBuilder} objects.
     *
     * @author Crosby
     * @since 0.2.4
     */
    @Redirect(method = "lambda$build$0", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/misc/MeteorStarscript;run(Lmeteordevelopment/starscript/Script;)Ljava/lang/String;"))
    private static String useStringBuilder(Script script) {
        return MeteorStarscript.run(script, stringBuilder);
    }
}
