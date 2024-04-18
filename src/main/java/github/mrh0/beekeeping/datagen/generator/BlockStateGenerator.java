package github.mrh0.beekeeping.datagen.generator;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Beehive;
import github.mrh0.beekeeping.bee.BeehiveRegistry;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockStateProvider;
import net.minecraft.data.PackOutput;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Beehive beehive : BeehiveRegistry.INSTANCE.getAll()) {
            simpleBlock(beehive.block, models().cubeBottomTop(beehive.getName(),
                    Beekeeping.get("block/beehives/" + beehive.species.name + "_side"),
                    Beekeeping.get("block/beehives/" + beehive.species.name + "_bottom"),
                    Beekeeping.get("block/beehives/" + beehive.species.name + "_top")
            ));
            System.out.println("\"block.beekeeping." + beehive.getName() + "\": \"" + Util.capitalize(beehive.getName()) + "\",");
        }
    }
}
