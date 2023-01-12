package io.github.racoondog.electron.utils;

import io.github.racoondog.electron.config.ElectronConfig;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.Pool;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
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

/**
 * TODO when committed to Meteor:
 * In order to increase precision in some cases (such as {@link meteordevelopment.meteorclient.systems.modules.combat.CrystalAura}, make {@link BlockIterator#register(int, int, BiConsumer)} take doubles instead of integers.
 * Internally, the {@link ChunkBlockIterator#hRadius} and {@link ChunkBlockIterator#vRadius} would still use ints using <code>(int) Math.ceil(radiusDouble)</>, but the {@link Callback} would use doubles for the distance calculation.
 * This would allow {@link meteordevelopment.meteorclient.systems.modules.combat.CrystalAura} use the correct default radius of 4.5 instead of 5, which would remove the need for double checking the distance.
 */
@Environment(EnvType.CLIENT)
public final class ChunkBlockIterator {

    private static final Pool<Callback> callbackPool = new Pool<>(Callback::new);
    private static final List<Callback> callbacks = new ArrayList<>();

    private static final List<Runnable> afterCallbacks = new ArrayList<>();

    private static final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private static int hRadius, vRadius;

    private static boolean disableCurrent;

    @PreInit
    public static void preInit() {
        if (!ElectronConfig.isDisabled("io.github.racoondog.electron.mixin.tick.blockiterator.BlockIteratorMixin")) {
            MeteorClient.EVENT_BUS.subscribe(ChunkBlockIterator.class);
            MeteorClient.EVENT_BUS.unsubscribe(BlockIterator.class);
        }
    }

    /* Iterator */

    @EventHandler(priority = EventPriority.LOWEST - 1)
    private static void onTick(TickEvent.Pre event) {
        if (!Utils.canUpdate() || mc.world.isDebugWorld() || hRadius == 0 || vRadius == 0) return;

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

        int cx1 = x1 >> 4;
        int cz1 = z1 >> 4;
        int cx2 = x2 >> 4;
        int cz2 = z2 >> 4;

        if (cx1 == cx2 && cz1 == cz2) singleChunkIterator(cx1, cz1, x1, y1, z1, x2, y2, z2, px, py, pz);
        else multiChunkIterator(cx1, cz1, cx2, cz2, x1, y1, z1, x2, y2, z2, px, py, pz);

        hRadius = 0;
        vRadius = 0;

        for (Callback callback : callbacks) callbackPool.free(callback);
        callbacks.clear();

        for (Runnable callback : afterCallbacks) callback.run();
        afterCallbacks.clear();
    }

    private static void multiChunkIterator(int cx1, int cz1, int cx2, int cz2, int x1, int y1, int z1, int x2, int y2, int z2, int px, int py, int pz) {
        for (int cx = cx1; cx <= cx2; cx++) {
            int chunkEdgeX = cx << 4;
            int chunkX1 = Math.max(chunkEdgeX, x1);
            int chunkX2 = Math.min(chunkEdgeX + 15, x2);

            for (int cz = cz1; cz <= cz2; cz++) {
                int chunkEdgeZ = cz << 4;
                int chunkZ1 = Math.max(chunkEdgeZ, z1);
                int chunkZ2 = Math.min(chunkEdgeZ + 15, z2);

                singleChunkIterator(cx, cz, chunkX1, y1, chunkZ1, chunkX2, y2, chunkZ2, px, py, pz);
            }
        }
    }

    private static void singleChunkIterator(int cx, int cz, int x1, int y1, int z1, int x2, int y2, int z2, int px, int py, int pz) {
        WorldChunk chunk = mc.world.getChunk(cx, cz);

        for (int y = y1; y <= y2; y++) {
            int sIndex = chunk.getSectionIndex(y);
            if (sIndex < 0 || sIndex >= chunk.getSectionArray().length) continue;
            ChunkSection section = chunk.getSection(sIndex);
            if (section.isEmpty()) continue;

            int ey = y & 15;
            blockPos.setY(y);
            int dy = Math.abs(y - py);

            for (int x = x1; x <= x2; x++) {
                int ex = x & 15;
                blockPos.setX(x);
                int dx = Math.abs(x - px);

                for (int z = z1; z <= z2; z++) {
                    blockPos.setZ(z);

                    BlockState blockState = section.getBlockState(ex, ey, z & 15);
                    int dz = Math.abs(z - pz);

                    if (callbacks(dx, dy, dz, blockState)) return;
                }
            }
        }
    }

    /* Callbacks */

    private static boolean callbacks(int dx, int dy, int dz, BlockState blockState) {
        for (Iterator<Callback> it = callbacks.iterator(); it.hasNext(); ) {
            Callback callback = it.next();

            if (dx <= callback.hRadius && dy <= callback.vRadius && dz <= callback.hRadius) {
                disableCurrent = false;
                callback.function.accept(blockPos, blockState);
                if (disableCurrent) {
                    it.remove();
                    if (callbacks.isEmpty()) return true;
                }
            }
        }

        return false;
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
