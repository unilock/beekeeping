package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
    public BlockLootTableProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.ANALYZER);
        dropSelf(ModBlocks.APIARY);
    }
}
