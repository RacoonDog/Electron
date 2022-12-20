package io.github.racoondog.electron.mixin.mixin;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.misc.Pool;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = RenderUtils.class, remap = false)
public abstract class RenderUtilsMixin {
    @Shadow @Final private static Pool<RenderUtils.RenderBlock> renderBlockPool;
    @Shadow @Final private static List<RenderUtils.RenderBlock> renderBlocks;

    /**
     * @author Crosby
     * @reason Fuse loops and remove redundant method.
     * @since 0.2.6
     */
    @SuppressWarnings("OverwriteModifiers")
    @Overwrite
    private static void onTick(TickEvent.Pre event) {
        if (renderBlocks.isEmpty()) return;

        for (Iterator<RenderUtils.RenderBlock> it = renderBlocks.iterator(); it.hasNext();) {
            RenderUtils.RenderBlock renderBlock = it.next();
            if (--renderBlock.ticks <= 0) {
                renderBlockPool.free(renderBlock);
                it.remove();
            }
        }
    }
}
