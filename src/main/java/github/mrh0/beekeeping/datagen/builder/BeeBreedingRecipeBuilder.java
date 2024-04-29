package github.mrh0.beekeeping.datagen.builder;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BeeBreedingRecipeBuilder implements RecipeBuilder {
    private final Species drone;
    private final Species princess;
    private final Species offspring;

    public BeeBreedingRecipeBuilder(Species drone, Species princess, Species offspring) {
        this.drone = drone;
        this.princess = princess;
        this.offspring = offspring;
    }

    @NotNull
    @Override
    public RecipeBuilder unlockedBy(@NotNull String name, @NotNull CriterionTriggerInstance trigger) {
        return this;
    }

    @NotNull
    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @NotNull
    @Override
    public Item getResult() {
        return offspring.getQueen().getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeConsumer, @NotNull ResourceLocation recipeId) {
        recipeConsumer.accept(new Result(drone, princess, offspring));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Species drone;
        private final Species princess;
        private final Species offspring;

        public Result(Species drone, Species princess, Species offspring) {
            this.id = new ResourceLocation(Beekeeping.MODID, "bee_breeding/" + drone.name + "_drone_with_" + princess.name + "_princess");
            this.drone = drone;
            this.princess = princess;
            this.offspring = offspring;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("drone", drone.name);
            json.addProperty("princess", princess.name);
            json.addProperty("offspring", offspring.name);
        }

        @NotNull
        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
            return BeeBreedingRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
