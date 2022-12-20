package io.github.racoondog.electron.mixin.mixin;

import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
@Mixin(value = TargetUtils.class, remap = false)
public abstract class TargetUtilsMixin {
    /**
     * Use repeated removal instead of iterating through elements with a predicate *ahem* removeIf *ahem*.
     *
     * @author Crosby
     * @since 0.2.6
     */
    @SuppressWarnings("ListRemoveInLoop")
    @Inject(method = "getList", at = @At(value = "INVOKE", target = "Ljava/util/List;removeIf(Ljava/util/function/Predicate;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private static void repeatedRemoval(List<Entity> targetList, Predicate<Entity> isGood, SortPriority sortPriority, int maxCount, CallbackInfo ci) {
        if (targetList.size() > maxCount) for (int i = maxCount; i < targetList.size(); i++) targetList.remove(maxCount);
        ci.cancel();
    }
}
