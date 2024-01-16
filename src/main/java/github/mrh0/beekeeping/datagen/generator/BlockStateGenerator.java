package github.mrh0.beekeeping.datagen.generator;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockStateProvider;
import net.minecraft.data.PackOutput;

public class BlockStateGenerator extends BlockStateProvider {
	public BlockStateGenerator(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
		super(output, modid, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for(Specie specie : SpeciesRegistry.instance.getAll()) {
			if(specie.hasBeehive()) {
				simpleBlock(specie.beehive.block.get(), models().cubeBottomTop(specie.getName() + "_beehive",
						Beekeeping.get("block/beehives/" + specie.getName() + "_side"),
						Beekeeping.get("block/beehives/" + specie.getName() + "_bottom"),
						Beekeeping.get("block/beehives/" + specie.getName() + "_top")
				));
				System.out.println("\"block.beekeeping." + specie.getName() + "_beehive\": \"" + Util.capitalize(specie.getName()) + " Beehive\",");
			}
		}
	}
}
