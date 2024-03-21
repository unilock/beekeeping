package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.registry.ModBlocks;
import github.mrh0.beekeeping.registry.ModTags;
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
        for(Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if(!species.hasBeehive()) continue;
            tag(ModTags.Blocks.BEEHIVES).add(BuiltInRegistries.BLOCK.getResourceKey(species.beehive.block).orElseThrow());
            tag(BlockTags.MINEABLE_WITH_AXE).add(BuiltInRegistries.BLOCK.getResourceKey(species.beehive.block).orElseThrow());
        }

        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.APIARY).orElseThrow());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.ANALYZER).orElseThrow());
    }
}
