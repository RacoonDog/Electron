package io.github.racoondog.electron.mixin.starscript;

import io.github.racoondog.electron.utils.NameUtils;
import meteordevelopment.meteorclient.utils.misc.HorizontalDirection;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.misc.Names;
import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.*;

import java.util.Formatter;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorStarscript.class, remap = false)
public abstract class MeteorStarscriptMixin {
    @Shadow @Final private static StringBuilder SB;
    @Unique private static final Formatter FORMATTER = new Formatter(SB);

    /**
     * Since Minecraft-related Starscript *should* only ever be called from the main thread, we can reuse a static {@link StringBuilder} and {@link Formatter} for speeeeeeeed.
     * @author Crosby
     * @since 0.3.5
     */
    @Unique private static String format(String format, Object... args) {
        SB.setLength(0);
        return FORMATTER.format(format, args).toString();
    }

    /**
     * @author Crosby
     * @reason Formatter + round beforehand +
     * @since 0.3.5
     */
    @Overwrite
    private static Value posString(double x, double y, double z) {
        return Value.string(format("X: %s Y: %s Z: %s", Math.round(x), Math.round(y), Math.round(z)));
    }


    /**
     * @author Crosby
     * @reason Don't precompute name & durability + use suppliers + static formatter
     * @since 0.3.5
     */
    @Overwrite
    public static Value wrap(ItemStack itemStack) {
        return Value.map(new ValueMap()
            .set("_toString", () -> {
                String name = itemStack.isEmpty() ? "" : Names.get(itemStack.getItem());
                return Value.string(itemStack.getCount() <= 1 ? name : format("%s %sx", name, itemStack.getCount()));
            })
            .set("name", () -> Value.string(itemStack.isEmpty() ? "" : Names.get(itemStack.getItem())))
            .set("id", () -> Value.string(Registries.ITEM.getId(itemStack.getItem()).toString()))
            .set("count", Value.number(itemStack.getCount()))
            .set("durability", () -> {
                int durability = 0;
                if (!itemStack.isEmpty() && itemStack.isDamageable()) durability = itemStack.getMaxDamage() - itemStack.getDamage();
                return Value.number(durability);
            })
            .set("max_durability", Value.number(itemStack.getMaxDamage()))
        );
    }

    /**
     * @author Crosby
     * @reason Use suppliers
     * @since 0.3.5
     */
    @Overwrite
    public static Value wrap(BlockPos blockPos, BlockState blockState) {
        return Value.map(new ValueMap()
            .set("_toString", () -> Value.string(Names.get(blockState.getBlock())))
            .set("id", () -> Value.string(Registries.BLOCK.getId(blockState.getBlock()).toString()))
            .set("pos", () -> Value.map(new ValueMap()
                .set("_toString", () -> posString(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .set("x", Value.number(blockPos.getX()))
                .set("y", Value.number(blockPos.getY()))
                .set("z", Value.number(blockPos.getZ()))
            ))
        );
    }

    /**
     * @author Crosby
     * @reason Use suppliers + cache entity names
     * @since 0.3.5
     */
    @Overwrite
    public static Value wrap(Entity entity) {
        return Value.map(new ValueMap()
            .set("_toString", () -> Value.string(NameUtils.get(entity)))
            .set("id", () -> Value.string(Registries.ENTITY_TYPE.getId(entity.getType()).toString()))
            .set("health", () -> Value.number(entity instanceof LivingEntity e ? e.getHealth() : 0))
            .set("pos", () -> Value.map(new ValueMap()
                .set("_toString", () -> posString(entity.getX(), entity.getY(), entity.getZ()))
                .set("x", Value.number(entity.getX()))
                .set("y", Value.number(entity.getY()))
                .set("z", Value.number(entity.getZ()))
            ))
        );
    }

    /**
     * @author Crosby
     * @reason Use suppliers
     * @since 0.3.5
     */
    @Overwrite
    public static Value wrap(HorizontalDirection dir) {
        return Value.map(new ValueMap()
            .set("_toString", () -> Value.string(dir.name + " " + dir.axis))
            .set("name", Value.string(dir.name))
            .set("axis", Value.string(dir.axis))
        );
    }
}
