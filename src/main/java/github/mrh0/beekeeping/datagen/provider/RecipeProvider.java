package github.mrh0.beekeeping.datagen.provider;

import com.mojang.datafixers.util.Pair;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.datagen.builder.BeeBreedingRecipeBuilder;
import github.mrh0.beekeeping.datagen.builder.BeeProduceRecipeBuilder;
import github.mrh0.beekeeping.item.ItemBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> rc) {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(specie.produce == null)
                continue;
            produce(rc, specie.getName(), specie.produce.common(), specie.produce.commonCountUnsatisfied(), specie.produce.commonCountSatisfied(),
                specie.produce.rare(), specie.produce.rareCountUnsatisfied(), specie.produce.rareCountSatisfied(), specie.produce.rareChanceUnsatisfied(), specie.produce.rareChanceSatisfied());

            for(Pair<String, String> pair : specie.breeding) {
                breed(rc, Specie.getByName(pair.getFirst()), Specie.getByName(pair.getSecond()), specie);
            }
        }

        /*ShapelessRecipeBuilder.shapeless(Index.BASIC_FRAME.get())
                .requires(Index.BASIC_FRAME.get())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Index.BASIC_FRAME.get()).build()))
                .save(rc);*/

        for(Pair<RecipeBuilder, ItemLike> pair : ItemBuilder.recipes) {
            pair.getFirst().unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                    .of(pair.getSecond()).build())).save(rc);
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
