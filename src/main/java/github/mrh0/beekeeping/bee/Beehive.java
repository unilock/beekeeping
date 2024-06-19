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
    public final BeehivePlacement placement;
    public final int minY;
    public final int maxY;

    public final PlacementModifier placementModifier;
    public final Feature<RandomPatchConfiguration> feature;
    public final Function<BlockPos, Boolean> allowPlacement;

    private Beehive(Species species, TagKey<Biome> biomeTag, int tries, int rarity, BeehivePlacement placement, int minY, int maxY) {
        this.species = species;
        this.biomeTag = biomeTag;
        this.tries = tries;
        this.rarity = rarity;
        this.placement = placement;
        this.minY = minY;
        this.maxY = maxY;

        if (placement == BeehivePlacement.NORMAL) {
            this.placementModifier = PlacementUtils.HEIGHTMAP;
            this.feature = new RandomPatchFeature(RandomPatchConfiguration.CODEC);
            this.allowPlacement = (pos) -> true;
        } else if (placement == BeehivePlacement.HEIGHT) {
            this.placementModifier = PlacementUtils.FULL_RANGE;
            this.feature = new RandomPatchFeature(RandomPatchConfiguration.CODEC);
            this.allowPlacement = (pos) -> pos.getY() > this.minY && pos.getY() < this.maxY;
        } else {
            throw new RuntimeException(); // TODO: replace with something better, i'm too tired for this
        }
    }

    public String getName() {
        return this.species.name + "_beehive";
    }

    public static Builder builder(Species species) {
        return new Builder(species);
    }

    public static class Builder {
        private final Species species;
        private TagKey<Biome> biomeTag = BiomeTags.IS_OVERWORLD;
        private int tries = 0;
        private int rarity = 0;
        private BeehivePlacement placement = BeehivePlacement.NORMAL;
        private int minY = -63;
        private int maxY = 255;

        private Builder(Species species) {
            this.species = species;
        }

        public Beehive build() {
            return new Beehive(this.species, this.biomeTag, this.tries, this.rarity, this.placement, this.minY, this.maxY);
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

        public Builder setPlacement(BeehivePlacement placement) {
            this.placement = placement;
            return this;
        }

        public Builder setMinHeight(int minY) {
            if (this.placement != BeehivePlacement.HEIGHT) throw new IllegalStateException();

            this.minY = minY;
            return this;
        }

        public Builder setMaxHeight(int maxY) {
            if (this.placement != BeehivePlacement.HEIGHT) throw new IllegalStateException();

            this.maxY = maxY;
            return this;
        }
    }
}
