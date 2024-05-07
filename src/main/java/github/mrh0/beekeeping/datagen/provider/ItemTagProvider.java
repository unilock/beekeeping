package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.item.frame.FrameItem;
import github.mrh0.beekeeping.registry.ModItems;
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
        tag(ModTags.Items.BEES)
                .add(BuiltInRegistries.ITEM.getResourceKey(ModItems.DRONE).orElseThrow())
                .add(BuiltInRegistries.ITEM.getResourceKey(ModItems.PRINCESS).orElseThrow())
                .add(BuiltInRegistries.ITEM.getResourceKey(ModItems.QUEEN).orElseThrow());

        for (FrameItem frame : FrameItem.frames) {
            tag(ModTags.Items.FRAMES).add(BuiltInRegistries.ITEM.getResourceKey(frame).orElseThrow());
        }
    }
}
