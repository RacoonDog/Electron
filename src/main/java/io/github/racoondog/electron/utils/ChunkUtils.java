package io.github.racoondog.electron.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

@Environment(EnvType.CLIENT)
public final class ChunkUtils {
    /**
     * Get a {@link BlockState} from a position within a {@link WorldChunk} using primitives.
     *
     * @author Crosby
     */
    public static BlockState getChunkBlockState(WorldChunk chunk, int x, int y, int z) {
        try {
            ChunkSection chunkSection;
            int l = chunk.getSectionIndex(y);
            if (l >= 0 && l < chunk.getSectionArray().length && !(chunkSection = chunk.getSection(l)).isEmpty()) {
                return chunkSection.getBlockState(x & 15, y & 15, z & 15);
            }
            return Blocks.AIR.getDefaultState();
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Getting block state");
            CrashReportSection crashReportSection = crashReport.addElement("Block being got");
            crashReportSection.add("Location", () -> CrashReportSection.createPositionString(chunk, x, y, z));
            throw new CrashException(crashReport);
        }
    }
}
