package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tags.BlockTags;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public BlockTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		for(Specie specie : SpeciesRegistry.instance.getAll()) {
			if(!specie.hasBeehive()) continue;
			tag(Index.BEEHIVE_TAG).add(specie.beehive.block.get());
			tag(BlockTags.MINEABLE_WITH_AXE).add(specie.beehive.block.get());
		}

		tag(BlockTags.MINEABLE_WITH_AXE)
				.add(Index.APIARY_BLOCK.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(Index.ANALYZER_BLOCK.get());
	}
}
