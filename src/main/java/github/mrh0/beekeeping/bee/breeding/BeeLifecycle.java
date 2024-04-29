package github.mrh0.beekeeping.bee.breeding;

import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.bee.genes.LightToleranceGene;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.genes.TemperatureToleranceGene;
import github.mrh0.beekeeping.bee.genes.WeatherToleranceGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class BeeLifecycle {
    public static Species getOffspringSpecies(Level level, ItemStack drone, ItemStack princess) {
        SimpleContainer inv = new SimpleContainer(2);
        inv.setItem(0, drone);
        inv.setItem(1, princess);

        Optional<BeeBreedingRecipe> match = level.getRecipeManager().getRecipeFor(BeeBreedingRecipe.Type.INSTANCE, inv, level);
        if (match.isEmpty()) {
            return Util.selectRandom(BeeItem.getSpecies(drone), BeeItem.getSpecies(princess));
        }
        return match.get().getOffspring();
    }

    public static CompoundTag getOffspringTag(ItemStack drone, ItemStack princess, Species offspring, SelectFunction fn) {
        CompoundTag tag = new CompoundTag();

        ItemStack queen = offspring.getQueen();

        BeeItem.init(
                queen,
                fn.select(LifetimeGene.get(drone), LifetimeGene.get(princess)),
                fn.select(WeatherToleranceGene.get(drone), WeatherToleranceGene.get(princess)),
                fn.select(TemperatureToleranceGene.get(drone), TemperatureToleranceGene.get(princess)),
                fn.select(LightToleranceGene.get(drone), LightToleranceGene.get(princess)),
                fn.select(RareProduceGene.get(drone), RareProduceGene.get(princess))
        );

        return tag;
    }

    public static Optional<BeeProduceRecipe> getProduceRecipe(Level level, ItemStack queen) {
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, queen);

        return level.getRecipeManager().getRecipeFor(BeeProduceRecipe.Type.INSTANCE, inv, level);
    }

    public static ItemStack clone(ItemStack bee, ItemStack type) {
        CompoundTag tag = new CompoundTag();

        BeeItem.init(
                bee,
                LifetimeGene.get(bee),
                WeatherToleranceGene.get(bee),
                TemperatureToleranceGene.get(bee),
                LightToleranceGene.get(bee),
                RareProduceGene.get(bee)
        );

        type.setTag(tag);

        return type;
    }

    public static ItemStack mutateRandom(ItemStack bee) {
        int selected = Gene.random.nextInt(0,5);
        boolean up = Gene.random.nextBoolean();

        return switch(selected) {
            case 0 -> up ? LifetimeGene.up(bee) : LifetimeGene.down(bee);
            case 1 -> up ? WeatherToleranceGene.up(bee) : WeatherToleranceGene.down(bee);
            case 2 -> up ? TemperatureToleranceGene.up(bee) : TemperatureToleranceGene.down(bee);
            case 3 -> up ? LightToleranceGene.up(bee) : LightToleranceGene.down(bee);
            case 4 -> up ? RareProduceGene.up(bee) : RareProduceGene.down(bee);
            default -> bee;
        };
    }

    public interface SelectFunction {
        int select(int a, int b);
    }
}
