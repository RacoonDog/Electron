package io.github.racoondog.electron.mixin.mixin;

import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Predicate;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(value = TargetUtils.class, remap = false)
public abstract class TargetUtilsMixin {
    @Shadow private static int sort(Entity e1, Entity e2, SortPriority priority) {throw new AssertionError();}

    /**
     * @author Crosby
     * @reason Use maxCount in a smart way.
     * @since 0.2.7
     */
    @SuppressWarnings("ListRemoveInLoop")
    @Overwrite
    public static void getList(List<Entity> targetList, Predicate<Entity> isGood, SortPriority sortPriority, int maxCount) {
        targetList.clear();

        for (var entity : mc.world.getEntities()) {
            if (entity != null && isGood.test(entity)) {
                targetList.add(entity);
                if (targetList.size() >= maxCount) break;
            }
        }

        if (targetList.size() < maxCount) FakePlayerManager.forEach(fp -> {
            if (fp != null && isGood.test(fp)) targetList.add(fp);
        });

        targetList.sort((e1, e2) -> sort(e1, e2, sortPriority));
        if (targetList.size() > maxCount) for (int i = maxCount; i < targetList.size(); i++) targetList.remove(maxCount);
    }
}
