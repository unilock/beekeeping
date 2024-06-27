package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.registry.ModBlocks;
import github.mrh0.beekeeping.registry.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

public class ItemGroup {
    public static CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = null;

    public static final CreativeModeTab BEES = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Beekeeping.get("bees"), FabricItemGroup.builder()
        .title(Component.translatable("itemGroup.beekeeping.bees"))
        .icon(Items.BEE_NEST::getDefaultInstance)
        .displayItems((itemDisplayParameters, output) -> {
            output.accept(ModBlocks.ANALYZER);
            output.accept(ModBlocks.APIARY);
            output.accept(ModItems.THERMOMETER);
            output.accept(ModItems.BASIC_FRAME);
            output.accept(ModItems.GLOWING_FRAME);
            output.accept(ModItems.INSULATED_FRAME);
            output.accept(ModItems.MUTATION_FRAME);
            output.accept(ModItems.WATERPROOF_FRAME);

            for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
                output.accept(species.getDrone());
                output.accept(species.getPrincess());
                output.accept(species.getQueen());
            }
        })
        .build());

    public static void init() {

    }
}
