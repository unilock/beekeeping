package github.mrh0.beekeeping.bee;

import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.function.Function;

public class Beehive {
    public final Species species;
    public final TagKey<Biome> biomeTag;
    public final int tries;
    public final int rarity;
    public final PlacementModifier modifier;
    public final Feature<RandomPatchConfiguration> feature;
    public final Function<BlockPos, Boolean> blockPlaceAllow;
    public BeehiveBlock block;

    public Beehive(Species species, TagKey<Biome> biomeTag, int tries, int rarity) {
        this.species = species;
        this.biomeTag = biomeTag;
        this.tries = tries;
        this.rarity = rarity;
        this.modifier = PlacementUtils.HEIGHTMAP;
        this.feature = new RandomPatchFeature(RandomPatchConfiguration.CODEC);
        this.blockPlaceAllow = pos -> true;
    }

    public Beehive(Species species, TagKey<Biome> biomeTag, int tries, int rarity, PlacementModifier modifier, Feature<RandomPatchConfiguration> feature, Function<BlockPos, Boolean> blockPlaceAllow) {
        this.species = species;
        this.biomeTag = biomeTag;
        this.tries = tries;
        this.rarity = rarity;
        this.modifier = modifier;
        this.feature = feature;
        this.blockPlaceAllow = blockPlaceAllow;
    }

    public String getName() {
        return species.getName() + "_beehive";
    }

    public boolean acceptsBiome(Holder<Biome> biome) {
        return biome.is(biomeTag);
    }
}
