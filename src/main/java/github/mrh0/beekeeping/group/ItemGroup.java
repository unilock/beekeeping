package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import net.fabricmc.fabric.api.event.Event;
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

	public static void register() {
		Event<ItemGroupEvents.ModifyEntries> event = ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Beekeeping.get("bees")));
		Index.ITEMS.getEntries().forEach(item -> event.register(entries -> entries.accept(item.get())));
		Index.BLOCKS.getEntries().forEach(item -> event.register(entries -> entries.accept(item.get())));
	}
}
