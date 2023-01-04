package io.github.racoondog.electron.mixin.starscript.section;

import io.github.racoondog.electron.utils.StarscriptUtils;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Script;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorStarscript.class, remap = false)
public abstract class MeteorStarscriptMixin {
    /**
     * @author Crosby
     * @reason Ignore {@link meteordevelopment.starscript.Section} instructions.
     * @since 0.2.4
     */
    @Overwrite
    public static String run(Script script, StringBuilder sb) {
        return StarscriptUtils.runRawString(script, sb);
    }

    /**
     * @author Crosby
     * @reason Ignore {@link meteordevelopment.starscript.Section} instructions.
     * @since 0.2.4
     */
    @Overwrite
    public static String run(Script script) {
        return StarscriptUtils.runRawString(script);
    }
}
