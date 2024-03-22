package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ItemGroup {
    public static final CreativeModeTab BEES = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Beekeeping.get("bees"), FabricItemGroup.builder()
        .title(Component.translatable("itemGroup.beekeeping.bees"))
        .icon(Items.BEE_NEST::getDefaultInstance)
        .build());

    private static final Event<ItemGroupEvents.ModifyEntries> EVENT = ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Beekeeping.get("bees")));

    public static void init() {

    }

    public static void add(Item item) {
        EVENT.register(entries -> entries.accept(item));
    }
}
