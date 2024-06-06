package github.mrh0.beekeeping.datagen.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
    private static final Path OUTPUT_SPECIES = Path.of("../../src/main/resources/data/beekeeping/species").toAbsolutePath();
    private static final Path OUTPUT_BEEHIVES = Path.of("../../src/main/resources/data/beekeeping/beehives").toAbsolutePath();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static void makeAll() throws IOException {
        if (!OUTPUT_SPECIES.toFile().exists()) {
            OUTPUT_SPECIES.toFile().mkdirs();
        }
        if (!OUTPUT_BEEHIVES.toFile().exists()) {
            OUTPUT_BEEHIVES.toFile().mkdirs();
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

        // Add the species' usual properties.
        json.addProperty("name", species.name);
        json.addProperty("color", species.color);
        json.addProperty("dark", species.dark);
        json.addProperty("foil", species.foil);
        json.addProperty("nocturnal", species.nocturnal);
        json.addProperty("preferred_temperature", species.preferredTemperature.name);

        // Add the species' gene selectors.
        JsonObject geneSelectors = new JsonObject();

        geneSelectors.addProperty("lifetime", species.lifetimeGene.name);
        geneSelectors.addProperty("light_tolerance", species.lightGene.name);
        geneSelectors.addProperty("rare_produce", species.produceGene.name);
        geneSelectors.addProperty("temperature_tolerance", species.temperatureGene.name);
        geneSelectors.addProperty("weather_tolerance", species.weatherGene.name);

        json.add("gene_selectors", geneSelectors);

        // Add the species' parents, if it has any.
        if (species.parents != null) {
            JsonArray parents = new JsonArray();

            parents.add(species.parents.getFirst());
            parents.add(species.parents.getSecond());

            json.add("parents", parents);
        }

        // Add the species' produce.
        JsonObject produce = new JsonObject();

        // Common produce...
        JsonObject commonProduce = new JsonObject();
        commonProduce.addProperty("item", BuiltInRegistries.ITEM.getKey(species.produce.common()).toString());

        JsonObject commonProduceCount = new JsonObject();
        commonProduceCount.addProperty("unsatisfied", species.produce.commonCountUnsatisfied());
        commonProduceCount.addProperty("satisfied", species.produce.commonCountSatisfied());

        commonProduce.add("count", commonProduceCount);
        produce.add("common", commonProduce);

        // Rare produce, if it exists...
        if (species.produce.rare() != Items.AIR) {
            JsonObject rareProduce = new JsonObject();
            rareProduce.addProperty("item", BuiltInRegistries.ITEM.getKey(species.produce.rare()).toString());

            JsonObject rareProduceCount = new JsonObject();
            rareProduceCount.addProperty("unsatisfied", species.produce.rareCountUnsatisfied());
            rareProduceCount.addProperty("satisfied", species.produce.rareCountSatisfied());

            JsonObject rareProduceChance = new JsonObject();
            rareProduceChance.addProperty("unsatisfied", species.produce.rareChanceUnsatisfied());
            rareProduceChance.addProperty("satisfied", species.produce.rareChanceSatisfied());

            rareProduce.add("count", rareProduceCount);
            rareProduce.add("chance", rareProduceChance);
            produce.add("rare", rareProduce);
        }

        json.add("produce", produce);

        // Write to the species file.
        File file = OUTPUT_SPECIES.resolve(species.name + ".json").toFile();

        try (FileWriter writer = new FileWriter(file)) {
            System.out.println(file.getCanonicalPath());
            GSON.toJson(json, writer);
        }

        return species;
    }

    private static Beehive makeBeehive(Beehive beehive) throws IOException {
        JsonObject json = new JsonObject();

        // Add the beehive's usual properties.
        json.addProperty("name", beehive.species.name);

        // Add the beehive's spawn conditions.
        JsonObject spawnConditions = new JsonObject();

        spawnConditions.addProperty("biome_tag", beehive.biomeTag.location().toString());
        spawnConditions.addProperty("tries", beehive.tries);
        spawnConditions.addProperty("rarity", beehive.rarity);
//        TODO: ???
//        spawnConditions.addProperty("placement_modifier", 0);
//        spawnConditions.addProperty("feature", 0);
//        spawnConditions.addProperty("placement_function", 0);

        json.add("spawn_conditions", spawnConditions);

        // Write to the species file.
        File file = OUTPUT_BEEHIVES.resolve(beehive.species.name + ".json").toFile();

        try (FileWriter writer = new FileWriter(file)) {
            System.out.println(file.getCanonicalPath());
            GSON.toJson(json, writer);
        }

        return beehive;
    }
}
