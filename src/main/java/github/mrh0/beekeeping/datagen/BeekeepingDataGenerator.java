package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.datagen.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BeekeepingDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(BeeBreedingRecipeProvider::new);
		fabricDataGenerator.addProvider(BeeProduceRecipeProvider::new);
		fabricDataGenerator.addProvider(BlockLootTableProvider::new);
		BlockTagProvider blockTagProvider = fabricDataGenerator.addProvider(BlockTagProvider::new);
		fabricDataGenerator.addProvider(new ItemTagProvider(fabricDataGenerator, blockTagProvider));
		fabricDataGenerator.addProvider(RecipeProvider::new);
	}
}
