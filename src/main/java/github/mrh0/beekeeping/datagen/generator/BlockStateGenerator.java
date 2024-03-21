package github.mrh0.beekeeping.datagen.generator;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Species;
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
        for(Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if(species.hasBeehive()) {
                simpleBlock(species.beehive.block, models().cubeBottomTop(species.getName() + "_beehive",
                        Beekeeping.get("block/beehives/" + species.getName() + "_side"),
                        Beekeeping.get("block/beehives/" + species.getName() + "_bottom"),
                        Beekeeping.get("block/beehives/" + species.getName() + "_top")
                ));
                System.out.println("\"block.beekeeping." + species.getName() + "_beehive\": \"" + Util.capitalize(species.getName()) + " Beehive\",");
            }
        }
    }
}
