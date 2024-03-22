package github.mrh0.beekeeping.screen.slot;

import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TagSlot extends Slot {
    private final TagKey<Item> tag;

    public TagSlot(SimpleContainer container, int index, int x, int y, TagKey<Item> tag) {
        super(container, index, x, y);
        this.tag = tag;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(tag);
    }
}
