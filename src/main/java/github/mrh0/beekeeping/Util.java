package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.genes.Gene;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class Util {
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String[] strs = str.replace('_', ' ').split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strs.length; i++) {
            builder.append(strs[i].substring(0, 1).toUpperCase()).append(strs[i].substring(1));
            if (i != strs.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    public static String camelCase(String str) {
        var words = str.replaceAll("_", " ").split(" ");
        var sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            sb.append(w.substring(0, 1).toUpperCase()).append(w.substring(1));
            if (i+1 < words.length) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @SafeVarargs
    public static <T> T selectRandom(T...items) {
        return items[Gene.random.nextInt(items.length)];
    }

    public static ItemStack rollChance(ItemStack stack, double chance) {
        if (Gene.random.nextDouble() < chance) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static int getSunlight(Level level, BlockPos pos) {
        int i = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
        return Mth.clamp(i, 0, 15);
    }
}
