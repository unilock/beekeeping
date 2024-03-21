package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlock;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlock;
import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModBlocks {
    public static final AnalyzerBlock ANALYZER = register("analyzer", new AnalyzerBlock());
    public static final ApiaryBlock APIARY = register("apiary", new ApiaryBlock());

    public static void init() {
        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if (species.hasBeehive()) {
                species.beehive.block = register(species.beehive.getName(), new BeehiveBlock(species));
            }
        }
    }

    private static <T extends Block> T register(String path, T block) {
        Registry.register(BuiltInRegistries.BLOCK, Beekeeping.get(path), block);
        Registry.register(BuiltInRegistries.ITEM, Beekeeping.get(path), new BlockItem(block, new FabricItemSettings()));
        return block;
    }
}
