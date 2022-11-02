package io.github.racoondog.electron.blockiterator;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.Pool;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public final class ChunkBlockIterator {

    private static final Pool<Callback> callbackPool = new Pool<>(Callback::new);
    private static final List<Callback> callbacks = new ArrayList<>();

    private static final List<Runnable> afterCallbacks = new ArrayList<>();

    private static final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private static int hRadius, vRadius;

    private static boolean disableCurrent;

    @EventHandler(priority = EventPriority.LOWEST - 1)
    private static void onTick(TickEvent.Pre event) {
        if (!Utils.canUpdate() || mc.world.isDebugWorld() || (hRadius == 0 && vRadius == 0)) return;

        int px = mc.player.getBlockX();
        int py = mc.player.getBlockY();
        int pz = mc.player.getBlockZ();

        int x1 = px - hRadius;
        int y1 = py - vRadius;
        int z1 = pz - hRadius;
        int x2 = px + hRadius;
        int y2 = py + vRadius;
        int z2 = pz + hRadius;

        y1 = MathHelper.clamp(y1, mc.world.getBottomY(), y2);
        y2 = MathHelper.clamp(y2, y1, mc.world.getTopY());

        loop:
        for (int cx = x1 >> 4; cx <= x2 >> 4; cx++) {
            for (int cz = z1 >> 4; cz <= z2 >> 4; cz++) {
                WorldChunk chunk = mc.world.getChunk(cx, cz);

                int chunkEdgeX = cx * 16;
                int chunkEdgeZ = cz * 16;

                int chunkX1 = Math.max(chunkEdgeX, x1);
                int chunkZ1 = Math.max(chunkEdgeZ, z1);
                int chunkX2 = Math.min(chunkEdgeX + 15, x2);
                int chunkZ2 = Math.min(chunkEdgeZ + 15, z2);

                for (int y = y1; y <= y2; y++) {
                    int sIndex = chunk.getSectionIndex(y);
                    if (sIndex < 0 || sIndex >= chunk.getSectionArray().length) continue;
                    ChunkSection section = chunk.getSection(sIndex);
                    if (section.isEmpty()) continue;

                    int ey = y & 15;
                    blockPos.setY(y);
                    int dy = Math.abs(y - py);

                    for (int x = chunkX1; x <= chunkX2; x++) {
                        int ex = x & 15;
                        blockPos.setX(x);
                        int dx = Math.abs(x - px);

                        for (int z = chunkZ1; z <= chunkZ2; z++) {
                            blockPos.setZ(z);
                            BlockState blockState = section.getBlockState(ex, ey, z & 15);
                            int dz = Math.abs(z - pz);

                            for (Iterator<Callback> it = callbacks.iterator(); it.hasNext(); ) {
                                Callback callback = it.next();

                                if (dx <= callback.hRadius && dy <= callback.vRadius && dz <= callback.hRadius) {
                                    disableCurrent = false;
                                    callback.function.accept(blockPos, blockState);
                                    if (disableCurrent) {
                                        it.remove();
                                        if (callbacks.isEmpty()) break loop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        hRadius = 0;
        vRadius = 0;

        for (Callback callback : callbacks) callbackPool.free(callback);
        callbacks.clear();

        for (Runnable callback : afterCallbacks) callback.run();
        afterCallbacks.clear();
    }

    public static void register(int horizontalRadius, int verticalRadius, BiConsumer<BlockPos, BlockState> function) {
        hRadius = Math.max(hRadius, horizontalRadius);
        vRadius = Math.max(vRadius, verticalRadius);

        Callback callback = callbackPool.get();

        callback.function = function;
        callback.hRadius = horizontalRadius;
        callback.vRadius = verticalRadius;

        callbacks.add(callback);
    }
    public static void disableCurrent() {
        disableCurrent = true;
    }

    public static void after(Runnable callback) {
        afterCallbacks.add(callback);
    }

    private static class Callback {
        public BiConsumer<BlockPos, BlockState> function;
        public int hRadius, vRadius;
    }
}
