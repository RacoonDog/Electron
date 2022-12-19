package io.github.racoondog.electron.mixin.mixin.starscript;

import io.github.racoondog.electron.utils.StarscriptUtils;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.utils.StarscriptError;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorStarscript.class, remap = false)
public abstract class MeteorStarscriptMixin {
    @Shadow public static Starscript ss;
    @Shadow public static void printChatError(StarscriptError error) {}

    /**
     * @author Crosby
     * @reason Ignore {@link meteordevelopment.starscript.Section} instructions.
     * @since 0.2.4
     */
    @Overwrite
    public static String run(Script script, StringBuilder sb) {
        try {
            return StarscriptUtils.getRawString(ss, script, sb);
        } catch (StarscriptError error) {
            printChatError(error);
            return null;
        }
    }

    /**
     * @author Crosby
     * @reason Ignore {@link meteordevelopment.starscript.Section} instructions.
     * @since 0.2.4
     */
    @Overwrite
    public static String run(Script script) {
        try {
            return StarscriptUtils.getRawString(ss, script);
        } catch (StarscriptError error) {
            printChatError(error);
            return null;
        }
    }
}
