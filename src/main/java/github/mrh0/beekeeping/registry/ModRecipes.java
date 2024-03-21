package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {
    public static final RecipeSerializer<BeeBreedingRecipe> BEE_BREEDING = register("bee_breeding", BeeBreedingRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<BeeProduceRecipe> BEE_PRODUCE = register("bee_produce", BeeProduceRecipe.Serializer.INSTANCE);

    public static void init() {

    }

    private static <T extends Recipe<SimpleContainer>> RecipeSerializer<T> register(String path, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Beekeeping.get(path), serializer);
    }
}
