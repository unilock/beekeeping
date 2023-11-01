package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

public class ItemGroup {
    public static final CreativeModeTab BEES = FabricItemGroupBuilder.create(Beekeeping.get(Beekeeping.MODID))
		.icon(Items.BEE_NEST::getDefaultInstance)
		.build();
}
