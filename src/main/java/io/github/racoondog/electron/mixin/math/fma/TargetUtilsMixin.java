package io.github.racoondog.electron.mixin.math.fma;

import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(TargetUtils.class)
public abstract class TargetUtilsMixin {
    /**
     * @author Crosby
     * @reason Math.FMA()
     * @since 0.3.0
     */
    @Overwrite
    private static int sortAngle(Entity e1, Entity e2) {
        boolean e1l = e1 instanceof LivingEntity;
        boolean e2l = e2 instanceof LivingEntity;

        if (!e1l && !e2l) return 0;
        else if (e1l && !e2l) return 1;
        else if (!e1l) return -1;

        double e1yaw = Math.abs(Rotations.getYaw(e1) - mc.player.getYaw());
        double e2yaw = Math.abs(Rotations.getYaw(e2) - mc.player.getYaw());

        double e1pitch = Math.abs(Rotations.getPitch(e1) - mc.player.getPitch());
        double e2pitch = Math.abs(Rotations.getPitch(e2) - mc.player.getPitch());

        return Double.compare(Math.sqrt(Math.fma(e1yaw, e1yaw, e1pitch * e1pitch)), Math.sqrt(Math.fma(e2yaw, e2yaw, e2pitch * e2pitch)));
    }
}
