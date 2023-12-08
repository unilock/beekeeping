package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.datagen.generator.BeeIconGenerator;
import github.mrh0.beekeeping.datagen.generator.BlockStateGenerator;
import github.mrh0.beekeeping.datagen.generator.ItemModelGenerator;
import github.mrh0.beekeeping.datagen.provider.BlockLootTableProvider;
import github.mrh0.beekeeping.datagen.provider.BlockTagProvider;
import github.mrh0.beekeeping.datagen.provider.ItemTagProvider;
import github.mrh0.beekeeping.datagen.provider.RecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;

public class BeekeepingDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		try {
			BeeIconGenerator.makeAll();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ExistingFileHelper existingFileHelper = ExistingFileHelper.withResourcesFromArg();

		var blockTags = generator.addProvider(BlockTagProvider::new);
		generator.addProvider(RecipeProvider::new);
		generator.addProvider(BlockLootTableProvider::new);
		generator.addProvider(new BlockStateGenerator(generator, existingFileHelper));
		generator.addProvider(new ItemModelGenerator(generator, existingFileHelper));
		generator.addProvider(new ItemTagProvider(generator, blockTags));
	}
}
