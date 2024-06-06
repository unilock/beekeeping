package github.mrh0.beekeeping.bee;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
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
    public final PlacementModifier placementModifier;
    public final Feature<RandomPatchConfiguration> feature;
    public final Function<BlockPos, Boolean> allowPlacement;

    private Beehive(Species species, TagKey<Biome> biomeTag, int tries, int rarity, PlacementModifier modifier, Feature<RandomPatchConfiguration> feature, Function<BlockPos, Boolean> blockPlaceAllow) {
        this.species = species;
        this.biomeTag = biomeTag;
        this.tries = tries;
        this.rarity = rarity;
        this.placementModifier = modifier;
        this.feature = feature;
        this.allowPlacement = blockPlaceAllow;
    }

    public String getName() {
        return this.species.name + "_beehive";
    }

    public static Builder builder(Species species) {
        return new Builder(species);
    }

    public static class Builder {
        final Species species;
        TagKey<Biome> biomeTag = BiomeTags.IS_OVERWORLD;
        int tries = 0;
        int rarity = 0;
        PlacementModifier placementModifier = PlacementUtils.HEIGHTMAP;
        Feature<RandomPatchConfiguration> feature = new RandomPatchFeature(RandomPatchConfiguration.CODEC);
        Function<BlockPos, Boolean> allowPlacement = (pos) -> true;

        private Builder(Species species) {
            this.species = species;
        }

        public Beehive build() {
            return new Beehive(this.species, this.biomeTag, this.tries, this.rarity, this.placementModifier, this.feature, this.allowPlacement);
        }

        public Builder setBiomeTag(TagKey<Biome> biomeTag) {
            this.biomeTag = biomeTag;
            return this;
        }

        public Builder setTries(int tries) {
            this.tries = tries;
            return this;
        }

        public Builder setRarity(int rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder setPlacementModifier(PlacementModifier placementModifier) {
            this.placementModifier = placementModifier;
            return this;
        }

        public Builder setFeature(Feature<RandomPatchConfiguration> feature) {
            this.feature = feature;
            return this;
        }

        public Builder setAllowPlacement(Function<BlockPos, Boolean> allowPlacement) {
            this.allowPlacement = allowPlacement;
            return this;
        }
    }
}
