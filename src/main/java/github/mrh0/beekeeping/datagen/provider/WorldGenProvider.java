package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends FabricDynamicRegistryProvider {
    public WorldGenProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        final HolderLookup.RegistryLookup<Biome> biomeRegistry = registries.lookupOrThrow(Registries.BIOME);

        entries.addAll(biomeRegistry);

        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if (species.hasBeehive()) {
                var configuredFeature = new ConfiguredFeature<>(
                        species.beehive.feature,
                        new RandomPatchConfiguration(
                                species.beehive.tries,
                                16,
                                2,
                                PlacementUtils.onlyWhenEmpty(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(species.beehive.block)
                                        )
                                )
                        )
                );

                var featureRef = entries.add(ResourceKey.create(Registries.CONFIGURED_FEATURE, Beekeeping.get(species.beehive.getName() + "_configured")), configuredFeature);

                var placedFeature = new PlacedFeature(
                        featureRef,
                        List.of(RarityFilter.onAverageOnceEvery(species.beehive.rarity), InSquarePlacement.spread(), species.beehive.modifier, BiomeFilter.biome())
                );

                entries.add(ResourceKey.create(Registries.PLACED_FEATURE, Beekeeping.get(species.beehive.getName() + "_placed")), placedFeature);
            }
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "WorldGen";
    }
}
