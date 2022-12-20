package io.github.racoondog.electron.mixin.mixin;

import meteordevelopment.meteorclient.systems.modules.world.PacketMine;
import meteordevelopment.meteorclient.utils.misc.Pool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
@Mixin(value = PacketMine.class, remap = false)
public abstract class PacketMineMixin {
    @Shadow @Final private Pool<PacketMine.MyBlock> blockPool;

    /**
     * Ensure pool objects are freed before removal.
     *
     * @author Crosby
     * @since 0.2.6
     */
    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Ljava/util/List;removeIf(Ljava/util/function/Predicate;)Z"))
    private boolean freePoolObjects(List<PacketMine.MyBlock> instance, Predicate<PacketMine.MyBlock> predicate) {
        for (Iterator<PacketMine.MyBlock> it = instance.iterator(); it.hasNext();) {
            PacketMine.MyBlock block = it.next();
            if (predicate.test(block)) {
                blockPool.free(block);
                it.remove();
            }
        }
        return false;
    }
}
