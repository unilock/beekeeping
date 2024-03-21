package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.Beekeeping;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final class Blocks {
        public static final TagKey<Block> BEEHIVES = create("beehives");

        private static TagKey<Block> create(String path) {
            return TagKey.create(Registries.BLOCK, Beekeeping.get(path));
        }
    }

    public static final class Items {
        public static final TagKey<Item> BEES = create("bees");
        public static final TagKey<Item> DRONES = create("drones");
        public static final TagKey<Item> PRINCESSES = create("princesses");
        public static final TagKey<Item> QUEENS = create("queens");
        public static final TagKey<Item> FRAMES = create("frames");

        private static TagKey<Item> create(String path) {
            return TagKey.create(Registries.ITEM, Beekeeping.get(path));
        }
    }

    public static void init() {

    }
}
