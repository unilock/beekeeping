package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.bee.Beehive;
import github.mrh0.beekeeping.bee.BeehiveRegistry;
import github.mrh0.beekeeping.bee.Produce;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.config.Config;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Items;

public class ModSpecies {
    //
    // TODO: data-driven
    //
    public static void init() {
        var r = SpeciesRegistry.INSTANCE;
        var b = BeehiveRegistry.INSTANCE;

        var common = r.register(Species.builder("common")
                .setColor(0xFFFFF2CC)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(3, 5)
                        .build())
                .build());
        b.register(Beehive.builder(common)
                .setBiomeTag(Tags.Biomes.IS_PLAINS)
                .setRarity(16)
                .setTries(2)
                .build());

        var forest = r.register(Species.builder("forest")
                .setColor(0xFF93C47D)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(3, 5)
                        .build())
                .build());
        b.register(Beehive.builder(forest)
                .setBiomeTag(BiomeTags.IS_FOREST)
                .setRarity(10)
                .setTries(4)
                .build());

        var tempered = r.register(Species.builder("tempered")
                .setColor(0xFFB6D7A8)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(7, 9)
                        .build())
                .setTemperatureGene(Gene::random5Narrow)
                .setParents("common", "forest")
                .build());

        var tropical = r.register(Species.builder("tropical")
                .setColor(0xFF6AA84F)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setLifetimeGene(Gene::random5Narrow)
                .build());
        b.register(Beehive.builder(tropical)
                .setBiomeTag(BiomeTags.IS_JUNGLE)
                .setRarity(10)
                .setTries(4)
                .build());

        var coco = r.register(Species.builder("coco")
                .setColor(0xFF783F04)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.COCOA_BEANS)
                        .setRareCount(3, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setParents("dugout", "tropical")
                .build());

        var upland = r.register(Species.builder("upland")
                .setColor(0xFFFF9900)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(3, 7)
                        .setRareItem(Items.HONEY_BLOCK)
                        .setRareCount(1, 2)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .build());
        b.register(Beehive.builder(upland)
                .setBiomeTag(BiomeTags.IS_SAVANNA)
                .setRarity(16)
                .setTries(4)
                .build());

        var dune = r.register(Species.builder("dune")
                .setColor(0xFFFBBC04)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .build());
        b.register(Beehive.builder(dune)
                .setBiomeTag(Tags.Biomes.IS_SANDY) // TODO: Fix biome tag sandy & hot
                .setRarity(12)
                .setTries(3)
                .build());

        var snowy = r.register(Species.builder("snowy")
                .setColor(0xFFEFEFEF)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.SNOWBALL)
                        .setRareCount(8 ,16)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setTemperatureGene(Gene::random3Low)
                .build());
        b.register(Beehive.builder(snowy)
                .setBiomeTag(Tags.Biomes.IS_SNOWY)
                .setRarity(16)
                .setTries(4)
                .build());

        var frozen = r.register(Species.builder("frozen")
                .setColor(0xFFD0E0E3)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.ICE)
                        .setRareCount(3, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setParents("dugout", "snowy")
                .build());

        var glacial = r.register(Species.builder("glacial")
                .setColor(0xFFA2C4C9)
                .setFoil()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5 ,7)
                        .setRareItem(Items.PACKED_ICE)
                        .setRareCount(3, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLDEST)
                .setParents("frozen", "nocturnal")
                .build());

        var fungal = r.register(Species.builder("fungal")
                .setColor(0xFF660000)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.RED_MUSHROOM)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setProduceGene(Gene::random5High)
                .setTemperatureGene(Gene::random3High)
                .build());
        b.register(Beehive.builder(fungal)
                .setBiomeTag(Tags.Biomes.IS_MUSHROOM) // TODO: fix biome tag mushroom & swamp
                .setRarity(10)
                .setTries(3)
                .build());

        var mossy = r.register(Species.builder("mossy")
                .setColor(0xFF38761D)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.MOSS_BLOCK)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setProduceGene(Gene::normal5)
                .setTemperatureGene(Gene::random3Low)
                .setParents("dugout", "fungal")
                .build());

        var fair = r.register(Species.builder("fair")
                .setColor(0xFF00FF00)
                .setFoil()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5 ,7)
                        .setRareItem(Items.SUGAR)
                        .setRareCount(8, 16)
                        .build())
                .setParents("coco", "snowy")
                .build());

        var dugout = r.register(Species.builder("dugout")
                .setColor(0xFF7F6000)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene::any)
                .setTemperatureGene(Gene::random3High)
                .build());
        b.register(Beehive.builder(dugout)
                .setBiomeTag(BiomeTags.IS_OVERWORLD)
                .setRarity(2)
                .setTries(3)
                .setAllowPlacement(pos -> pos.getY() > Config.BEEHIVE_DUGOUT_MIN_HEIGHT.get() && pos.getY() < Config.BEEHIVE_DUGOUT_MAX_HEIGHT.get())
                .setPlacementModifier(PlacementUtils.FULL_RANGE)
                .build());

        var nocturnal = r.register(Species.builder("nocturnal")
                .setColor(0xFF073763)
                .setNocturnal()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.GLOWSTONE_DUST)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setParents("dugout", "ender")
                .build());

        var malignant = r.register(Species.builder("malignant")
                .setColor(0xFF999999)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.BONE_MEAL)
                        .setRareCount(3, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .build());
        b.register(Beehive.builder(malignant)
                .setBiomeTag(Tags.Biomes.IS_WASTELAND) // TODO: fix biome tag mesa & wasteland
                .setRarity(8)
                .setTries(1)
                .build());

        var wicked = r.register(Species.builder("wicked")
                .setColor(0xFF666666)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.SPIDER_EYE)
                        .setRareCount(1, 3)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setParents("malignant", "upland")
                .build());

        var withered = r.register(Species.builder("withered")
                .setColor(0xFF434343)
                .setDark()
                .setFoil()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.WITHER_SKELETON_SKULL)
                        .setRareCount(1, 1)
                        .setRareChance(0.02, 0.05)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setParents("demonic", "wicked")
                .build());

        var scorched = r.register(Species.builder("scorched")
                .setColor(0xFFFF9900)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.COAL)
                        .setRareCount(2, 5)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .build());
        b.register(Beehive.builder(scorched)
                .setBiomeTag(BiomeTags.IS_NETHER)
                .setRarity(6)
                .setTries(6)
                .setAllowPlacement(pos -> pos.getY() > Config.BEEHIVE_SCORCHED_MIN_HEIGHT.get() && pos.getY() < Config.BEEHIVE_SCORCHED_MAX_HEIGHT.get())
                .setPlacementModifier(PlacementUtils.FULL_RANGE)
                .build());

        var magmatic = r.register(Species.builder("magmatic")
                .setColor(0xFFFF6D01)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.MAGMA_CREAM)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setParents("dune", "scorched")
                .build());

        var infernal = r.register(Species.builder("infernal")
                .setColor(0xFFFF0000)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.GUNPOWDER)
                        .setRareCount(1, 3)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setParents("magmatic", "wicked")
                .build());

        var demonic = r.register(Species.builder("demonic")
                .setColor(0xFF990000)
                .setDark()
                .setFoil()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.BLAZE_POWDER)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setParents("infernal", "nocturnal")
                .build());

        var ender = r.register(Species.builder("ender")
                .setColor(0xFF134F5C)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.ENDER_PEARL)
                        .setRareCount(1, 1)
                        .setRareChance(0.2, 0.4)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene::any)
                .build());
        b.register(Beehive.builder(ender)
                .setBiomeTag(BiomeTags.IS_END)
                .setRarity(16)
                .setTries(2)
                .build());
    }
}
