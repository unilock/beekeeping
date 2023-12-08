package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.item.frame.FrameItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import org.jetbrains.annotations.Nullable;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ItemTagProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
		super(dataGenerator, blockTagProvider);
	}

	@Override
	protected void generateTags() {
		for(Specie specie : SpeciesRegistry.instance.getAll()) {
			tag(Index.BEES_TAG)
				.add(specie.droneItem)
				.add(specie.princessItem)
				.add(specie.queenItem);
			tag(Index.DRONE_BEES_TAG).add(specie.droneItem);
			tag(Index.PRINCESS_BEES_TAG).add(specie.princessItem);
			tag(Index.QUEEN_BEES_TAG).add(specie.queenItem);
		}
		for(FrameItem frame : FrameItem.frames) {
			tag(Index.FRAME_TAG).add(frame);
		}
	}
}
