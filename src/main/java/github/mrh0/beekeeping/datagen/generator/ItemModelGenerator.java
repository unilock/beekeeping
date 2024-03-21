package github.mrh0.beekeeping.datagen.generator;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.item.frame.FrameItem;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.item.ItemModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.item.ItemModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(Species species : SpeciesRegistry.INSTANCE.getAll()) {
            simpleItem(species.droneItem);
            simpleItem(species.princessItem);
            simpleItem(species.queenItem);

            if(species.hasBeehive())
                blockItem(species.beehive.block);

            System.out.println("\"item.beekeeping." + species.getName() + "_drone\": \"" + Util.capitalize(species.getName()) + " Drone\",");
            System.out.println("\"item.beekeeping." + species.getName() + "_princess\": \"" + Util.capitalize(species.getName()) + " Princess\",");
            System.out.println("\"item.beekeeping." + species.getName() + "_queen\": \"" + Util.capitalize(species.getName()) + " Queen\",");
            System.out.println("\"item.beekeeping.species." + species.getName() + "\": \"" + Util.capitalize(species.getName()) + "\",");
        }

        System.out.println();

        for(FrameItem frame : FrameItem.frames) {
            simpleItem(frame, "frame/");
            System.out.println("\"item.beekeeping." + frame.name + "_frame" + "\": \"" + Util.camelCase(frame.name) + " Frame" + "\",");
        }
    }

    private ItemModelBuilder simpleItem(Item item) {
        return simpleItem(item, "");
    }

    private ItemModelBuilder simpleItem(Item item, String path) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Beekeeping.MODID,"item/" + path + BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item).getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Beekeeping.MODID,"item/" + BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    private ItemModelBuilder blockItem(Block block) {
        var resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        return withExistingParent(resourceLocation.getPath(),
                new ResourceLocation(Beekeeping.MODID, "block/" + resourceLocation.getPath()));
    }
}
