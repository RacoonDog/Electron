package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.utils.ChunkUtils;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(value = EntityUtils.class, remap = false)
public abstract class EntityUtilsMixin {
    /**
     * @author Crosby
     * @reason Cache {@link WorldChunk} object and use primitives instead of {@link BlockPos}.
     */
    @Overwrite
    public static boolean isAboveWater(Entity entity) {
        if (entity.getBlockPos().getY() < mc.world.getBottomY()) return false;

        WorldChunk chunk = mc.world.getChunk(ChunkSectionPos.getSectionCoord(entity.getBlockPos().getX()), ChunkSectionPos.getSectionCoord(entity.getBlockPos().getZ()));
        int minY = Math.max(mc.world.getBottomY(), entity.getBlockY() - 64);

        for (int y = entity.getBlockY(); y < minY; y--) {
            if (y < mc.world.getBottomY()) break;

            BlockState state = ChunkUtils.getChunkBlockState(chunk, entity.getBlockX(), y, entity.getBlockZ());

            if (state.isOf(Blocks.WATER)) return true;
        }
        return false;
    }
}
