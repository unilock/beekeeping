package github.mrh0.beekeeping.datagen;

import com.mojang.datafixers.util.Pair;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.item.ItemBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> rc) {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(specie.produce == null)
                continue;
            produce(rc, specie.getName(), specie.produce.common(), specie.produce.commonCountUnsatisfied(), specie.produce.commonCountSatisfied(),
                specie.produce.rare(), specie.produce.rareCountUnsatisfied(), specie.produce.rareCountSatisfied(), specie.produce.rareChanceUnsatisfied(), specie.produce.rareChanceSatisfied());

            for(Pair<String, String> pair : specie.breeding) {
                breed(rc, Specie.getByName(pair.getFirst()), Specie.getByName(pair.getSecond()), specie);
            }
        }

        for(RecipeBuilder builder : ItemBuilder.recipes) {
            builder.save(rc);
        }
    }

    private void breed(Consumer<FinishedRecipe> recipeConsumer, Specie drone, Specie princess, Specie offspring) {
        new BeeBreedingRecipeBuilder(drone, princess, offspring)
                .save(recipeConsumer);
    }

    private void produce(Consumer<FinishedRecipe> recipeConsumer, String specie, Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare,
                         int rareCountUnsatisfied, int rareCountSatisfied,
                         double rareChanceUnsatisfied, double rareChanceSatisfied) {
        new BeeProduceRecipeBuilder(Specie.getByName(specie), new ItemStack(common, commonCountUnsatisfied), new ItemStack(rare, rareCountUnsatisfied), rareChanceUnsatisfied,
                new ItemStack(common, commonCountSatisfied), new ItemStack(rare, rareCountSatisfied), rareChanceSatisfied)
                .save(recipeConsumer);
    }
}

