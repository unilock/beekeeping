package github.mrh0.beekeeping.datagen.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import github.mrh0.beekeeping.bee.Beehive;
import github.mrh0.beekeeping.bee.Produce;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.config.Config;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Items;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BeeSpeciesGenerator {
    private static final Path OUTPUT = Path.of("../../src/main/resources/data/beekeeping/species").toAbsolutePath();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static void makeAll() throws IOException {
        if (!OUTPUT.toFile().exists()) {
            OUTPUT.toFile().mkdirs();
        }

        // TODO: everything

        var common = makeSpecies(Species.builder("common")
                .setColor(0xFFFFF2CC)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(3, 5)
                        .build())
                .build());
        makeBeehive(Beehive.builder(common)
                .setBiomeTag(Tags.Biomes.IS_PLAINS)
                .setRarity(16)
                .setTries(2)
                .build());

        var forest = makeSpecies(Species.builder("forest")
                .setColor(0xFF93C47D)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(3, 5)
                        .build())
                .build());
        makeBeehive(Beehive.builder(forest)
                .setBiomeTag(BiomeTags.IS_FOREST)
                .setRarity(10)
                .setTries(4)
                .build());

        var tempered = makeSpecies(Species.builder("tempered")
                .setColor(0xFFB6D7A8)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(7, 9)
                        .build())
                .setTemperatureGene(Gene.RandomFunctions.RANDOM_5_NARROW)
                .setParents("common", "forest")
                .build());

        var tropical = makeSpecies(Species.builder("tropical")
                .setColor(0xFF6AA84F)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setLifetimeGene(Gene.RandomFunctions.RANDOM_5_NARROW)
                .build());
        makeBeehive(Beehive.builder(tropical)
                .setBiomeTag(BiomeTags.IS_JUNGLE)
                .setRarity(10)
                .setTries(4)
                .build());

        var coco = makeSpecies(Species.builder("coco")
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

        var upland = makeSpecies(Species.builder("upland")
                .setColor(0xFFFF9900)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(3, 7)
                        .setRareItem(Items.HONEY_BLOCK)
                        .setRareCount(1, 2)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLifetimeGene(Gene.RandomFunctions.RANDOM_5_NARROW)
                .setWeatherGene(Gene.RandomFunctions.STRICT)
                .build());
        makeBeehive(Beehive.builder(upland)
                .setBiomeTag(BiomeTags.IS_SAVANNA)
                .setRarity(16)
                .setTries(4)
                .build());

        var dune = makeSpecies(Species.builder("dune")
                .setColor(0xFFFBBC04)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLifetimeGene(Gene.RandomFunctions.RANDOM_5_NARROW)
                .setWeatherGene(Gene.RandomFunctions.STRICT)
                .build());
        makeBeehive(Beehive.builder(dune)
                .setBiomeTag(Tags.Biomes.IS_SANDY) // TODO: Fix biome tag sandy & hot
                .setRarity(12)
                .setTries(3)
                .build());

        var snowy = makeSpecies(Species.builder("snowy")
                .setColor(0xFFEFEFEF)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.SNOWBALL)
                        .setRareCount(8 ,16)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setTemperatureGene(Gene.RandomFunctions.RANDOM_3_LOW)
                .build());
        makeBeehive(Beehive.builder(snowy)
                .setBiomeTag(Tags.Biomes.IS_SNOWY)
                .setRarity(16)
                .setTries(4)
                .build());

        var frozen = makeSpecies(Species.builder("frozen")
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

        var glacial = makeSpecies(Species.builder("glacial")
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

        var fungal = makeSpecies(Species.builder("fungal")
                .setColor(0xFF660000)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.RED_MUSHROOM)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setProduceGene(Gene.RandomFunctions.RANDOM_5_HIGH)
                .setTemperatureGene(Gene.RandomFunctions.RANDOM_3_HIGH)
                .build());
        makeBeehive(Beehive.builder(fungal)
                .setBiomeTag(Tags.Biomes.IS_MUSHROOM) // TODO: fix biome tag mushroom & swamp
                .setRarity(10)
                .setTries(3)
                .build());

        var mossy = makeSpecies(Species.builder("mossy")
                .setColor(0xFF38761D)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.MOSS_BLOCK)
                        .setRareCount(1, 1)
                        .setRareChance(0.5, 0.8)
                        .build())
                .setProduceGene(Gene.RandomFunctions.NORMAL_5)
                .setTemperatureGene(Gene.RandomFunctions.RANDOM_3_LOW)
                .setParents("dugout", "fungal")
                .build());

        var fair = makeSpecies(Species.builder("fair")
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

        var dugout = makeSpecies(Species.builder("dugout")
                .setColor(0xFF7F6000)
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .build())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene.RandomFunctions.ANY)
                .setTemperatureGene(Gene.RandomFunctions.RANDOM_3_HIGH)
                .build());
        makeBeehive(Beehive.builder(dugout)
                .setBiomeTag(BiomeTags.IS_OVERWORLD)
                .setRarity(2)
                .setTries(3)
                .setAllowPlacement(pos -> pos.getY() > Config.BEEHIVE_DUGOUT_MIN_HEIGHT.get() && pos.getY() < Config.BEEHIVE_DUGOUT_MAX_HEIGHT.get())
                .setPlacementModifier(PlacementUtils.FULL_RANGE)
                .build());

        var nocturnal = makeSpecies(Species.builder("nocturnal")
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

        var malignant = makeSpecies(Species.builder("malignant")
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
        makeBeehive(Beehive.builder(malignant)
                .setBiomeTag(Tags.Biomes.IS_WASTELAND) // TODO: fix biome tag mesa & wasteland
                .setRarity(8)
                .setTries(1)
                .build());

        var wicked = makeSpecies(Species.builder("wicked")
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

        var withered = makeSpecies(Species.builder("withered")
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

        var scorched = makeSpecies(Species.builder("scorched")
                .setColor(0xFFFF9900)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.COAL)
                        .setRareCount(2, 5)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene.RandomFunctions.ANY)
                .build());
        makeBeehive(Beehive.builder(scorched)
                .setBiomeTag(BiomeTags.IS_NETHER)
                .setRarity(6)
                .setTries(6)
                .setAllowPlacement(pos -> pos.getY() > Config.BEEHIVE_SCORCHED_MIN_HEIGHT.get() && pos.getY() < Config.BEEHIVE_SCORCHED_MAX_HEIGHT.get())
                .setPlacementModifier(PlacementUtils.FULL_RANGE)
                .build());

        var magmatic = makeSpecies(Species.builder("magmatic")
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
                .setLightGene(Gene.RandomFunctions.ANY)
                .setParents("dune", "scorched")
                .build());

        var infernal = makeSpecies(Species.builder("infernal")
                .setColor(0xFFFF0000)
                .setDark()
                .setProduce(Produce.builder()
                        .setCommonItem(Items.HONEYCOMB)
                        .setCommonCount(5, 7)
                        .setRareItem(Items.GUNPOWDER)
                        .setRareCount(1, 3)
                        .build())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene.RandomFunctions.ANY)
                .setParents("magmatic", "wicked")
                .build());

        var demonic = makeSpecies(Species.builder("demonic")
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
                .setLightGene(Gene.RandomFunctions.ANY)
                .setParents("infernal", "nocturnal")
                .build());

        var ender = makeSpecies(Species.builder("ender")
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
                .setLightGene(Gene.RandomFunctions.ANY)
                .build());
        makeBeehive(Beehive.builder(ender)
                .setBiomeTag(BiomeTags.IS_END)
                .setRarity(16)
                .setTries(2)
                .build());
    }

    private static Species makeSpecies(Species species) throws IOException {
        JsonObject json = new JsonObject();

        json.addProperty("name", species.name);
        json.addProperty("color", species.color);
        json.addProperty("dark", species.dark);
        json.addProperty("foil", species.foil);
        json.addProperty("nocturnal", species.nocturnal);
        json.addProperty("preferred_temperature", species.preferredTemperature.name);
        json.addProperty("lifetime_gene", species.lifetimeGene.name);
        json.addProperty("light_gene", species.lightGene.name);
        json.addProperty("produce_gene", species.produceGene.name);
        json.addProperty("temperature_gene", species.temperatureGene.name);
        json.addProperty("weather_gene", species.weatherGene.name);

        JsonObject produce = new JsonObject();

        produce.addProperty("common", BuiltInRegistries.ITEM.getKey(species.produce.common()).toString());
        produce.addProperty("common_count_unsatisfied", species.produce.commonCountUnsatisfied());
        produce.addProperty("common_count_satisfied", species.produce.commonCountSatisfied());
        produce.addProperty("rare", BuiltInRegistries.ITEM.getKey(species.produce.rare()).toString());
        produce.addProperty("rare_count_unsatisfied", species.produce.rareCountUnsatisfied());
        produce.addProperty("rare_count_satisfied", species.produce.rareCountSatisfied());
        produce.addProperty("rare_chance_unsatisfied", species.produce.rareChanceUnsatisfied());
        produce.addProperty("rare_chance_satisfied", species.produce.rareChanceSatisfied());

        json.add("produce", produce);

        File file = OUTPUT.resolve(species.name + ".json").toFile();

        System.out.println(file.getCanonicalPath());
        GSON.toJson(json, new FileWriter(file));

        return species;
    }

    private static void makeBeehive(Beehive beehive) {

    }
}
