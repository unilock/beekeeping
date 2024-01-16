package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(!specie.hasBeehive()) continue;
            tag(Index.BEEHIVE_TAG).add(BuiltInRegistries.BLOCK.getResourceKey(specie.beehive.block.get()).orElseThrow());
            tag(BlockTags.MINEABLE_WITH_AXE).add(BuiltInRegistries.BLOCK.getResourceKey(specie.beehive.block.get()).orElseThrow());
        }

        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(BuiltInRegistries.BLOCK.getResourceKey(Index.APIARY_BLOCK.get()).orElseThrow());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BuiltInRegistries.BLOCK.getResourceKey(Index.ANALYZER_BLOCK.get()).orElseThrow());
    }
}
