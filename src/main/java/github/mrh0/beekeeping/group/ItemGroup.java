package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

public class ItemGroup {
    public static final CreativeModeTab BEES = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Beekeeping.get("bees"), FabricItemGroup.builder()
        .title(Component.translatable("itemGroup.beekeeping.bees"))
        .icon(Items.BEE_NEST::getDefaultInstance)
        .build());

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Beekeeping.get("bees"))).register(entries -> {
            BuiltInRegistries.ITEM.keySet().forEach(key -> {
                if (Beekeeping.MODID.equals(key.getNamespace())) {
                    entries.accept(BuiltInRegistries.ITEM.get(key));
                }
            });
        });
    }
}
