package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.registry.ModBlocks;
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
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.APIARY).orElseThrow())
            .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.BEEHIVE).orElseThrow());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.ANALYZER).orElseThrow());
    }
}
