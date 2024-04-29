package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.item.frame.FrameItem;
import github.mrh0.beekeeping.registry.ModTags;
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
        // TODO
//        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
//            tag(ModTags.Items.BEES)
//                .add(BuiltInRegistries.ITEM.getResourceKey(species.droneItem).orElseThrow())
//                .add(BuiltInRegistries.ITEM.getResourceKey(species.princessItem).orElseThrow())
//                .add(BuiltInRegistries.ITEM.getResourceKey(species.queenItem).orElseThrow());
//            tag(ModTags.Items.DRONES).add(BuiltInRegistries.ITEM.getResourceKey(species.droneItem).orElseThrow());
//            tag(ModTags.Items.PRINCESSES).add(BuiltInRegistries.ITEM.getResourceKey(species.princessItem).orElseThrow());
//            tag(ModTags.Items.QUEENS).add(BuiltInRegistries.ITEM.getResourceKey(species.queenItem).orElseThrow());
//        }

        for (FrameItem frame : FrameItem.frames) {
            tag(ModTags.Items.FRAMES).add(BuiltInRegistries.ITEM.getResourceKey(frame).orElseThrow());
        }
    }
}
