package github.mrh0.beekeeping.datagen_old;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.item.frame.FrameItem;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Beekeeping.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            simpleItem(specie.droneItem);
            simpleItem(specie.princessItem);
            simpleItem(specie.queenItem);

            if(specie.hasBeehive())
                blockItem(specie.beehive.block.get());

            System.out.println("\"item.beekeeping." + specie.getName() + "_drone\": \"" + Util.capitalize(specie.getName()) + " Drone\",");
            System.out.println("\"item.beekeeping." + specie.getName() + "_princess\": \"" + Util.capitalize(specie.getName()) + " Princess\",");
            System.out.println("\"item.beekeeping." + specie.getName() + "_queen\": \"" + Util.capitalize(specie.getName()) + " Queen\",");
            System.out.println("\"item.beekeeping.species." + specie.getName() + "\": \"" + Util.capitalize(specie.getName()) + "\",");
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
        return withExistingParent(Registry.ITEM.getKey(item).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Beekeeping.MODID,"item/" + path + Registry.ITEM.getKey(item).getPath()));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(Registry.ITEM.getKey(item).getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Beekeeping.MODID,"item/" + Registry.ITEM.getKey(item).getPath()));
    }

    private ItemModelBuilder blockItem(Block block) {
        var resourceLocation = Registry.BLOCK.getKey(block);
        return withExistingParent(resourceLocation.getPath(),
                new ResourceLocation(Beekeeping.MODID, "block/" + resourceLocation.getPath()));
    }
}
