package github.mrh0.beekeeping.world.feature;

import github.mrh0.beekeeping.bee.Beehive;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class BeekeepingPlacedFeatures {
    public static Holder<PlacedFeature> getPlacedFeatures(Beehive beehive) {
        return PlacementUtils.register("beehive",
                BeekeepingConfiguredFeatures.getConfiguredFeatures(beehive), RarityFilter.onAverageOnceEvery(16),
                InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }
    //final Holder<PlacedFeature> BEEHIVE =
}