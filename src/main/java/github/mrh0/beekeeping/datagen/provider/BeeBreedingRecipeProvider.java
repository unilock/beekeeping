package github.mrh0.beekeeping.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class BeeBreedingRecipeProvider extends FabricRecipeProvider {
	public BeeBreedingRecipeProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateRecipes(Consumer<FinishedRecipe> exporter) {

	}
}
