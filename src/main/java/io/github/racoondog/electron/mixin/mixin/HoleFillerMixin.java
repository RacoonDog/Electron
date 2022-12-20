package io.github.racoondog.electron.mixin.mixin;

import com.google.common.collect.Lists;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.combat.HoleFiller;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(value = HoleFiller.class, remap = false)
public abstract class HoleFillerMixin {
    @Shadow @Final private Setting<Boolean> predict;
    @Shadow @Final private Setting<Integer> blocksPerTick;

    /**
     * @author Crosby
     * @reason Directly use primitives instead of creating multiple immutable objects.
     * @since 0.2.5
     */
    @Overwrite
    private double distance(PlayerEntity player, BlockPos pos, boolean feet) {
        double x = player.getX(), y = player.getY(), z = player.getZ();

        if (!feet) y += player.getEyeHeight(mc.player.getPose());
        else if (predict.get()) {
            x += player.getX() - player.prevX;
            y += player.getY() - player.prevY;
            z += player.getZ() - player.prevZ;
        }

        x -= pos.getX() + 0.5;
        y -= pos.getY() + ((feet) ? 1 : 0.5);
        z -= pos.getZ() + 0.5;

        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Replace with optimized implementation, remove looping element.
     *
     * @author Crosby
     * @since 0.2.5
     */
    @Redirect(method = "validHole", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<Entity> replaceCheck(ClientWorld instance, Entity entity, Box box, Predicate<Entity> predicate) {
        if (EntityUtils.intersectsWithEntity(box, predicate)) return Lists.newArrayList(entity); // Non-empty list
        else return new ArrayList<>(); // Empty list
    }

    // Break out of BlockIterator

    @Unique private int counter;

    /**
     * Reset counter at the start of every tick.
     *
     * @author Crosby
     * @since 0.2.5
     */
    @Inject(method = "onTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/world/BlockIterator;register(IILjava/util/function/BiConsumer;)V", shift = At.Shift.BEFORE))
    private void setCounter(TickEvent.Pre event, CallbackInfo ci) {
        this.counter = 0;
    }

    /**
     * Increment counter when hole found.
     *
     * @author Crosby
     * @since 0.2.5
     */
    @Inject(method = "lambda$onTick$5", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void incrementCounter(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        this.counter++;
    }

    /**
     * Break out of {@link BlockIterator} if counter is higher than max blocks filled per tick.
     *
     * @author Crosby
     * @since 0.2.5
     */
    @Inject(method = "lambda$onTick$5", at = @At("TAIL"))
    private void breakLoop(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (this.counter >= this.blocksPerTick.get()) BlockIterator.disableCurrent();
    }
}
