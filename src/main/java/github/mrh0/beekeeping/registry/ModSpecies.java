package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.config.Config;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class ModSpecies {
    //
    // TODO: data-driven
    //
    public static void init() {
        var r = SpeciesRegistry.INSTANCE;

        r.register(new Species("common", 0xFFfff2cc)
                .setProduce(Items.HONEYCOMB, 3, 5)
                .addBeehive(Tags.Biomes.IS_PLAINS, 2, 16));

        r.register(new Species("forest", 0xFF93c47d)
                .setProduce(Items.HONEYCOMB, 3, 5)
                .addBeehive(BiomeTags.IS_FOREST, 4, 10));

        r.register(new Species("tempered", 0xFFb6d7a8)
                .setProduce(Items.HONEYCOMB, 7, 9)
                .setTemperatureGene(Gene::random5Narrow)
                .breedFrom("common", "forest"));

        r.register(new Species("tropical", 0xFF6aa84f)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(BiomeTags.IS_JUNGLE, 4, 10)
                .setLifetimeGene(Gene::random5Narrow)
                .setPreferredTemperature(BiomeTemperature.WARM));

        r.register(new Species("coco", 0xFF783f04)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.COCOA_BEANS, 3, 7)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .breedFrom("tropical", "dugout"));

        r.register(new Species("upland", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 3, 7, Items.HONEY_BLOCK, 1, 2)
                .addBeehive(BiomeTags.IS_SAVANNA, 4, 16)
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Species("dune", 0xFFfbbc04)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(Tags.Biomes.IS_SANDY, 3, 12) // TODO: Fix biome tag sandy & hot
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Species("snowy", 0xFFefefef)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SNOWBALL, 8, 16)
                .addBeehive(Tags.Biomes.IS_SNOWY, 4, 16)
                .setTemperatureGene(Gene::random3Low)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Species("frozen", 0xFFd0e0e3)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.ICE, 3, 7)
                .setPreferredTemperature(BiomeTemperature.COLD)
                .breedFrom("snowy", "dugout"));

        r.register(new Species("glacial", 0xFFa2c4c9)
                .setFoil()
                .setProduce(Items.HONEYCOMB, 5, 7, Items.PACKED_ICE, 3, 7)
                .setPreferredTemperature(BiomeTemperature.COLDEST)
                .breedFrom("frozen", "nocturnal"));

        r.register(new Species("fungal", 0xFF660000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.RED_MUSHROOM, 0.5d, 0.8d)
                .addBeehive(Tags.Biomes.IS_MUSHROOM, 3, 10) // TODO: fix biome tag mushroom & swamp
                .setTemperatureGene(Gene::random3High)
                .setProduceGene(Gene::random5High));

        r.register(new Species("mossy", 0xFF38761d)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.MOSS_BLOCK, 0.5d, 0.8d)
                .setTemperatureGene(Gene::random3Low)
                .setProduceGene(Gene::normal5)
                .breedFrom("dugout", "fungal"));

        r.register(new Species("fair", 0xFF00ff00)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SUGAR, 8, 16)
                .breedFrom("coco", "snowy")
                .setFoil());

        r.register(new Species("dugout", 0xFF7f6000)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(BiomeTags.IS_OVERWORLD, 3, 2, PlacementUtils.FULL_RANGE, new RandomPatchFeature(RandomPatchConfiguration.CODEC), pos -> pos.getY() > Config.BEEHIVE_DUGOUT_MIN_HEIGHT.get() && pos.getY() < Config.BEEHIVE_DUGOUT_MAX_HEIGHT.get())
                .setTemperatureGene(Gene::random3High)
                .setLightGene(Gene::any)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Species("nocturnal", 0xFF073763)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.GLOWSTONE_DUST, 0.5d, 0.8d)
                .setNocturnal()
                .breedFrom("ender", "dugout"));

        r.register(new Species("malignant", 0xFF999999)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.BONE_MEAL, 3, 7)
                .addBeehive(Tags.Biomes.IS_WASTELAND, 1, 8) // TODO: fix biome tag mesa & wasteland
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark());

        r.register(new Species("wicked", 0xFF666666)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SPIDER_EYE, 1, 3)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark()
                .breedFrom("malignant", "upland"));

        r.register(new Species("withered", 0xFF434343)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.WITHER_SKELETON_SKULL, 0.02, 0.05)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark()
                .setFoil()
                .breedFrom("wicked", "demonic"));

        r.register(new Species("scorched", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.COAL, 2, 5)
                .addBeehive(BiomeTags.IS_NETHER, 6, 6, PlacementUtils.FULL_RANGE, new RandomPatchFeature(RandomPatchConfiguration.CODEC), pos -> pos.getY() > Config.BEEHIVE_SCORCHED_MIN_HEIGHT.get() && pos.getY() < Config.BEEHIVE_SCORCHED_MAX_HEIGHT.get())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark());

        r.register(new Species("magmatic", 0xFFff6d01)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.MAGMA_CREAM, 0.5d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("scorched", "dune"));

        r.register(new Species("infernal", 0xFFff0000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.GUNPOWDER, 1, 3)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("magmatic", "wicked"));

        r.register(new Species("demonic", 0xFF990000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.BLAZE_POWDER, 0.5d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setFoil()
                .setDark()
                .breedFrom("infernal", "nocturnal"));

        r.register(new Species("ender", 0xFF134f5c)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.ENDER_PEARL, 0.2d, 0.4d)
                .addBeehive(BiomeTags.IS_END, 2, 16)
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene::any)
                .setDark());
    }
}
