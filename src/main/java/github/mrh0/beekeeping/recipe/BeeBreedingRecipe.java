package github.mrh0.beekeeping.recipe;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.item.BeeItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class BeeBreedingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Species drone, princess, offspring;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack output;

    public BeeBreedingRecipe(ResourceLocation id, Species drone, Species princess, Species offspring) {
        this.id = id;
        this.drone = drone;
        this.princess = princess;
        this.offspring = offspring;
        this.recipeItems = NonNullList.create();//NonNullList.of(Ingredient.of(drone.droneItem), Ingredient.of(princess.princessItem));
        recipeItems.add(Ingredient.of(drone.droneItem));
        recipeItems.add(Ingredient.of(princess.princessItem));

        this.output = new ItemStack(offspring.queenItem);
    }

    public Species getOffspring() {
        return this.offspring;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return BeeItem.is(container.getItem(0), drone) && BeeItem.is(container.getItem(1), princess);
    }

    @Override
    public String getGroup() {
        return "bee_breeding";
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BeeBreedingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return Serializer.ID.toString();
        }
    }

    public static class Serializer implements RecipeSerializer<BeeBreedingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Beekeeping.MODID,"bee_breeding");

        @Override
        public BeeBreedingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Species droneSpecies = SpeciesRegistry.INSTANCE.get(GsonHelper.getAsString(json, "drone"));
            Species princessSpecies = SpeciesRegistry.INSTANCE.get(GsonHelper.getAsString(json, "princess"));
            Species offspringSpecies = SpeciesRegistry.INSTANCE.get(GsonHelper.getAsString(json, "offspring"));

            return new BeeBreedingRecipe(id, droneSpecies, princessSpecies, offspringSpecies);
        }

        @Override
        public BeeBreedingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Species droneSpecies = SpeciesRegistry.INSTANCE.get(buf.readUtf());
            Species princessSpecies = SpeciesRegistry.INSTANCE.get(buf.readUtf());
            Species offspringSpecies = SpeciesRegistry.INSTANCE.get(buf.readUtf());
            return new BeeBreedingRecipe(id, droneSpecies, princessSpecies, offspringSpecies);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BeeBreedingRecipe recipe) {
            buf.writeUtf(recipe.drone.name);
            buf.writeUtf(recipe.princess.name);
            buf.writeUtf(recipe.offspring.name);
        }
    }
}
