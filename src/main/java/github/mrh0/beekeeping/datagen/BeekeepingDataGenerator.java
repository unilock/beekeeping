package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.datagen.generator.BeeIconGenerator;
import github.mrh0.beekeeping.datagen.generator.BeeSpeciesGenerator;
import github.mrh0.beekeeping.datagen.generator.BlockStateGenerator;
import github.mrh0.beekeeping.datagen.generator.ItemModelGenerator;
import github.mrh0.beekeeping.datagen.provider.BlockLootTableProvider;
import github.mrh0.beekeeping.datagen.provider.BlockTagProvider;
import github.mrh0.beekeeping.datagen.provider.ItemTagProvider;
import github.mrh0.beekeeping.datagen.provider.RecipeProvider;
import github.mrh0.beekeeping.datagen.provider.WorldGenProvider;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.io.IOException;

public class BeekeepingDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        try {
            BeeIconGenerator.makeAll();
            BeeSpeciesGenerator.makeAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExistingFileHelper existingFileHelper = ExistingFileHelper.withResourcesFromArg();
        FabricDataGenerator.Pack pack = generator.createPack();

        var blockTags = pack.addProvider(BlockTagProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider((output, registriesFuture) -> new BlockStateGenerator(output, Beekeeping.MODID, existingFileHelper));
        pack.addProvider((output, registriesFuture) -> new ItemModelGenerator(output, Beekeeping.MODID, existingFileHelper));
        pack.addProvider((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTags));
        pack.addProvider(WorldGenProvider::new);
    }
}
