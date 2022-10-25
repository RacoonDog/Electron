package io.github.racoondog.electron.mixin;

import meteordevelopment.meteorclient.systems.modules.render.BetterTooltips;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.CLIENT)
@Mixin(value = Utils.class, remap = false)
public abstract class UtilsMixin {
    /**
     * @author Crosby
     * @reason Rewritten to use pattern variables and remove duplicate type casting.
     */
    @Overwrite
    public static Color getShulkerColor(ItemStack shulkerItem) {
        if (shulkerItem.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block == Blocks.ENDER_CHEST) return BetterTooltips.ECHEST_COLOR;
            if (block instanceof ShulkerBoxBlock shulkerBlock) {
                DyeColor dye = shulkerBlock.getColor();
                if (dye == null) return Utils.WHITE;
                final float[] colors = dye.getColorComponents();
                return new Color(colors[0], colors[1], colors[2], 1f);
            }
        }
        return Utils.WHITE;
    }

    /**
     * @author Crosby
     * @reason Remove unused loop iterations and redundant matrix population.
     */
    @Overwrite
    public static int levenshteinDistance(String from, String to, int insCost, int subCost, int delCost) {
        int textLength = from.length();
        int filterLength = to.length();

        if (textLength == 0) return filterLength * insCost;
        if (filterLength == 0) return textLength * delCost;

        // Populate matrix
        int[][] d = new int[textLength + 1][filterLength + 1];

        for (int i = 1; i <= textLength; i++) {
            d[i][0] = i * delCost;
        }

        for (int j = 1; j <= filterLength; j++) {
            d[0][j] = j * insCost;
        }

        // Find best route
        for (int i = 1; i <= textLength; i++) {
            for (int j = 1; j <= filterLength; j++) {
                int sCost = d[i-1][j-1] + (from.charAt(i-1) == to.charAt(j-1) ? 0 : subCost);
                int dCost = d[i-1][j] + delCost;
                int iCost = d[i][j-1] + insCost;
                d[i][j] = Math.min(Math.min(dCost, iCost), sCost);
            }
        }

        return d[textLength][filterLength];
    }
}
