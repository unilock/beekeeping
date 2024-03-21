package github.mrh0.beekeeping.datagen.builder;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BeeProduceRecipeBuilder implements RecipeBuilder {
    private final Species species;
    private final ItemStack commonProduceUnsatisfied;
    private final ItemStack rareProduceUnsatisfied;
    private final double rareChanceUnsatisfied;

    private final ItemStack commonProduceSatisfied;
    private final ItemStack rareProduceSatisfied;
    private final double rareChanceSatisfied;

    public BeeProduceRecipeBuilder(Species species,
                                   ItemStack commonProduceUnsatisfied, ItemStack rareProduceUnsatisfied, double rareChanceUnsatisfied,
                                   ItemStack commonProduceSatisfied, ItemStack rareProduceSatisfied, double rareChanceSatisfied) {
        this.species = species;
        this.commonProduceUnsatisfied = commonProduceUnsatisfied;
        this.rareProduceUnsatisfied = rareProduceUnsatisfied;
        this.rareChanceUnsatisfied = rareChanceUnsatisfied;

        this.commonProduceSatisfied = commonProduceSatisfied;
        this.rareProduceSatisfied = rareProduceSatisfied;
        this.rareChanceSatisfied = rareChanceSatisfied;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return commonProduceUnsatisfied.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation recipeId) {
        recipeConsumer.accept(new Result(recipeId, species,
                commonProduceUnsatisfied, rareProduceUnsatisfied, rareChanceUnsatisfied,
                commonProduceSatisfied, rareProduceSatisfied, rareChanceSatisfied));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Species species;
        private final ItemStack commonProduceUnsatisfied;
        private final ItemStack rareProduceUnsatisfied;
        private final double rareChanceUnsatisfied;

        private final ItemStack commonProduceSatisfied;
        private final ItemStack rareProduceSatisfied;
        private final double rareChanceSatisfied;

        public Result(ResourceLocation id, Species species,
                      ItemStack commonProduceUnsatisfied, ItemStack rareProduceUnsatisfied, double rareChanceUnsatisfied,
                      ItemStack commonProduceSatisfied, ItemStack rareProduceSatisfied, double rareChanceSatisfied) {
            this.id = id;
            this.species = species;
            this.commonProduceUnsatisfied = commonProduceUnsatisfied;
            this.rareProduceUnsatisfied = rareProduceUnsatisfied;
            this.rareChanceUnsatisfied = rareChanceUnsatisfied;

            this.commonProduceSatisfied = commonProduceSatisfied;
            this.rareProduceSatisfied = rareProduceSatisfied;
            this.rareChanceSatisfied = rareChanceSatisfied;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("species", species.getName());
            JsonObject produce = new JsonObject();
            json.add("produce", produce);

            produce.add("unsatisfied", makeProduce(commonProduceUnsatisfied, rareProduceUnsatisfied, rareChanceUnsatisfied));
            produce.add("satisfied", makeProduce(commonProduceSatisfied, rareProduceSatisfied, rareChanceSatisfied));
        }

        private JsonObject makeProduce(@Nullable ItemStack commonProduce, @Nullable ItemStack rareProduce, double rareChance) {
            JsonObject obj = new JsonObject();
            JsonObject common = new JsonObject();
            JsonObject rare = new JsonObject();

            if(commonProduce != null && !commonProduce.isEmpty()) {
                var resourceLocation = BuiltInRegistries.ITEM.getKey(commonProduce.getItem());
                obj.add("common", common);
                common.addProperty("item", resourceLocation.toString());
                common.addProperty("count", commonProduce.getCount());
            }

            if(rareProduce != null && rareChance > 0d) {
                var resourceLocation = BuiltInRegistries.ITEM.getKey(rareProduce.getItem());
                obj.add("rare", rare);

                rare.addProperty("item", resourceLocation.toString());
                rare.addProperty("chance", rareChance);
                rare.addProperty("count", rareProduce.getCount());
            }
            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Beekeeping.MODID, "bee_produce/" + species.getName());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BeeProduceRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
