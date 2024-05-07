package github.mrh0.beekeeping.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends FabricDynamicRegistryProvider {
    public WorldGenProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        final HolderLookup.RegistryLookup<Biome> biomeRegistry = registries.lookupOrThrow(Registries.BIOME);

        entries.addAll(biomeRegistry);

        // TODO
//        for (Beehive beehive : BeehiveRegistry.INSTANCE.getAll()) {
//            var configuredFeature = new ConfiguredFeature<>(
//                    beehive.feature,
//                    new RandomPatchConfiguration(
//                            beehive.tries,
//                            16,
//                            2,
//                            PlacementUtils.onlyWhenEmpty(
//                                    Feature.SIMPLE_BLOCK,
//                                    new SimpleBlockConfiguration(
//                                            BlockStateProvider.simple(beehive.block)
//                                    )
//                            )
//                    )
//            );
//
//            var featureRef = entries.add(ResourceKey.create(Registries.CONFIGURED_FEATURE, Beekeeping.get(beehive.getName() + "_configured")), configuredFeature);
//
//            var placedFeature = new PlacedFeature(
//                    featureRef,
//                    List.of(RarityFilter.onAverageOnceEvery(beehive.rarity), InSquarePlacement.spread(), beehive.placementModifier, BiomeFilter.biome())
//            );
//
//            entries.add(ResourceKey.create(Registries.PLACED_FEATURE, Beekeeping.get(beehive.getName() + "_placed")), placedFeature);
//        }
    }

    @NotNull
    @Override
    public String getName() {
        return "WorldGen";
    }
}
