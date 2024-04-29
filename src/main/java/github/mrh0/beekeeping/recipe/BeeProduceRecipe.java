package github.mrh0.beekeeping.recipe;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.item.BeeItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class BeeProduceRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Species species;
    private final ItemStack commonProduceUnsatisfied;
    private final ItemStack rareProduceUnsatisfied;
    private final double rareChanceUnsatisfied;

    private final ItemStack commonProduceSatisfied;
    private final ItemStack rareProduceSatisfied;
    private final double rareChanceSatisfied;

    private final NonNullList<Ingredient> recipeItems;

    public BeeProduceRecipe(
            ResourceLocation id, Species species,
            ItemStack commonProduceSatisfied, ItemStack rareProduceSatisfied, double rareChanceSatisfied,
            ItemStack commonProduceUnsatisfied, ItemStack rareProduceUnsatisfied, double rareChanceUnsatisfied
    ) {
        this.id = id;
        this.species = species;
        this.commonProduceUnsatisfied = commonProduceUnsatisfied;
        this.rareProduceUnsatisfied = rareProduceUnsatisfied;
        this.rareChanceUnsatisfied = rareChanceUnsatisfied;

        this.commonProduceSatisfied = commonProduceSatisfied;
        this.rareProduceSatisfied = rareProduceSatisfied;
        this.rareChanceSatisfied = rareChanceSatisfied;

        this.recipeItems = NonNullList.create();
        this.recipeItems.add(Ingredient.of(species.getQueen()));
    }

    public Species getSpecies() {
        return species;
    }

    public ItemStack getCommonProduce(boolean satisfied) {
        return satisfied ? commonProduceSatisfied.copy() : commonProduceUnsatisfied.copy();
    }

    public ItemStack getRareProduce(boolean satisfied) {
        return satisfied ? rareProduceSatisfied.copy() : rareProduceUnsatisfied.copy();
    }

    public double getRareChance(boolean satisfied) {
        return satisfied ? rareChanceSatisfied : rareChanceUnsatisfied;
    }

    public ItemStack getRolledRareProduce(boolean satisfied, double bonus) {
        double chance = getRareChance(satisfied) * bonus;
        if(chance >= 1d)
            return getRareProduce(satisfied);
        return Util.rollChance(getRareProduce(satisfied), chance);
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return BeeItem.is(container.getItem(0), species);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY.copy();
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

    public static class Type implements RecipeType<BeeProduceRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return Serializer.ID.toString();
        }
    }

    public static class Serializer implements RecipeSerializer<BeeProduceRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Beekeeping.MODID,"bee_produce");

        @Override
        public BeeProduceRecipe fromJson(ResourceLocation id, JsonObject json) {
            Species species = SpeciesRegistry.INSTANCE.get(GsonHelper.getAsString(json, "species"));

            JsonObject produce = GsonHelper.getAsJsonObject(json, "produce");
            JsonObject satisfied = GsonHelper.getAsJsonObject(produce, "satisfied");
            JsonObject unsatisfied = GsonHelper.getAsJsonObject(produce, "unsatisfied");

            ItemStack commonProduceUnsatisfied = ItemStack.EMPTY;
            if(unsatisfied.has("common"))
                commonProduceUnsatisfied = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(unsatisfied, "common"));

            Item rareProduceUnsatisfied = Items.AIR;
            int rareProduceUnsatisfiedCount = 1;
            double rareChanceUnsatisfied = 0;
            if(unsatisfied.has("rare")) {
                rareChanceUnsatisfied = 1;
                JsonObject rareUnsatisfied = GsonHelper.getAsJsonObject(unsatisfied, "rare");

                rareProduceUnsatisfied = ShapedRecipe.itemFromJson(rareUnsatisfied);
                if(rareUnsatisfied.has("count"))
                    rareProduceUnsatisfiedCount = GsonHelper.getAsInt(rareUnsatisfied, "count");
                if(rareUnsatisfied.has("chance"))
                    rareChanceUnsatisfied = GsonHelper.getAsDouble(rareUnsatisfied, "chance");
            }

            ItemStack commonProduceSatisfied = ItemStack.EMPTY;
            if(satisfied.has("common"))
                commonProduceSatisfied = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(satisfied, "common"));
            Item rareProduceSatisfied = Items.AIR;
            int rareProduceSatisfiedCount = 1;
            double rareChanceSatisfied = 0;
            if(satisfied.has("rare")) {
                rareChanceSatisfied = 1;
                JsonObject rareSatisfied = GsonHelper.getAsJsonObject(satisfied, "rare");

                rareProduceSatisfied = ShapedRecipe.itemFromJson(rareSatisfied);
                if(rareSatisfied.has("count"))
                    rareProduceSatisfiedCount = GsonHelper.getAsInt(rareSatisfied, "count");
                if(rareSatisfied.has("chance"))
                    rareChanceSatisfied = GsonHelper.getAsDouble(rareSatisfied, "chance");
            }

            return new BeeProduceRecipe(id, species,
                    commonProduceSatisfied, new ItemStack(rareProduceSatisfied, rareProduceSatisfiedCount), rareChanceSatisfied,
                    commonProduceUnsatisfied, new ItemStack(rareProduceUnsatisfied, rareProduceUnsatisfiedCount), rareChanceUnsatisfied);
        }

        @Override
        public BeeProduceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Species species = SpeciesRegistry.INSTANCE.get(buf.readUtf());
            ItemStack commonProduceUnsatisfied = buf.readItem();
            ItemStack rareProduceUnsatisfied = buf.readItem();
            double rareChanceUnsatisfied = buf.readDouble();

            ItemStack commonProduceSatisfied = buf.readItem();
            ItemStack rareProduceSatisfied = buf.readItem();
            double rareChanceSatisfied = buf.readDouble();

            return new BeeProduceRecipe(id, species,
                    commonProduceSatisfied, rareProduceSatisfied, rareChanceSatisfied,
                    commonProduceUnsatisfied, rareProduceUnsatisfied, rareChanceUnsatisfied);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BeeProduceRecipe recipe) {
            buf.writeUtf(recipe.species.name);
            buf.writeItem(recipe.commonProduceUnsatisfied);
            buf.writeItem(recipe.rareProduceUnsatisfied);
            buf.writeDouble(recipe.rareChanceUnsatisfied);
            buf.writeItem(recipe.commonProduceSatisfied);
            buf.writeItem(recipe.rareProduceSatisfied);
            buf.writeDouble(recipe.rareChanceSatisfied);
        }
    }
}
