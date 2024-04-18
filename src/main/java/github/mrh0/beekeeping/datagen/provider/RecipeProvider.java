package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.datagen.builder.BeeBreedingRecipeBuilder;
import github.mrh0.beekeeping.datagen.builder.BeeProduceRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> rc) {
        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if (species.produce != null) {
                produce(rc, species.name, species.produce.common(), species.produce.commonCountUnsatisfied(), species.produce.commonCountSatisfied(), species.produce.rare(), species.produce.rareCountUnsatisfied(), species.produce.rareCountSatisfied(), species.produce.rareChanceUnsatisfied(), species.produce.rareChanceSatisfied());
            }

            if (species.parents != null) {
                breed(rc, SpeciesRegistry.INSTANCE.get(species.parents.getFirst()), SpeciesRegistry.INSTANCE.get(species.parents.getSecond()), species);
            }
        }

        /*
        ShapelessRecipeBuilder.shapeless(Index.BASIC_FRAME.get())
                .requires(Index.BASIC_FRAME.get())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Index.BASIC_FRAME.get()).build()))
                .save(rc);
         */
    }

    private void breed(Consumer<FinishedRecipe> recipeConsumer, Species drone, Species princess, Species offspring) {
        new BeeBreedingRecipeBuilder(drone, princess, offspring).save(recipeConsumer);
        new BeeBreedingRecipeBuilder(princess, drone, offspring).save(recipeConsumer);
    }

    private void produce(Consumer<FinishedRecipe> recipeConsumer, String species, Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare, int rareCountUnsatisfied, int rareCountSatisfied, double rareChanceUnsatisfied, double rareChanceSatisfied) {
        new BeeProduceRecipeBuilder(SpeciesRegistry.INSTANCE.get(species), new ItemStack(common, commonCountUnsatisfied), new ItemStack(rare, rareCountUnsatisfied), rareChanceUnsatisfied, new ItemStack(common, commonCountSatisfied), new ItemStack(rare, rareCountSatisfied), rareChanceSatisfied).save(recipeConsumer);
    }
}
