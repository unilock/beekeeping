package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.item.frame.FrameItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            tag(Index.BEES_TAG)
                .add(BuiltInRegistries.ITEM.getResourceKey(specie.droneItem).orElseThrow())
                .add(BuiltInRegistries.ITEM.getResourceKey(specie.princessItem).orElseThrow())
                .add(BuiltInRegistries.ITEM.getResourceKey(specie.queenItem).orElseThrow());
            tag(Index.DRONE_BEES_TAG).add(BuiltInRegistries.ITEM.getResourceKey(specie.droneItem).orElseThrow());
            tag(Index.PRINCESS_BEES_TAG).add(BuiltInRegistries.ITEM.getResourceKey(specie.princessItem).orElseThrow());
            tag(Index.QUEEN_BEES_TAG).add(BuiltInRegistries.ITEM.getResourceKey(specie.queenItem).orElseThrow());
        }
        for(FrameItem frame : FrameItem.frames) {
            tag(Index.FRAME_TAG).add(BuiltInRegistries.ITEM.getResourceKey(frame).orElseThrow());
        }
    }
}
