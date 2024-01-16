package github.mrh0.beekeeping.world.gen;

import github.mrh0.beekeeping.Beekeeping;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;

public class BeehiveBiomeModifier {
	private static final BiomeModification BIOME_MODIFICATION = BiomeModifications.create(Beekeeping.get("biome_modification"));

    public static void modify() {
		/*
        // TODO: this is probably not ideal and we should probably move all the beehive placement into data jsons akin to:
        // https://github.com/MinecraftForge/MinecraftForge/blob/1.19.x/src/generated_test/resources/data/biome_modifiers_test/forge/biome_modifier/add_basalt.json
        for (Specie species : SpeciesRegistry.instance.getAll()) {
            if (species.hasBeehive()) {
				var configuredFeature = FeatureUtils.register(
					Beekeeping.get(species.beehive.getName() + "_configured").toString(),
					species.beehive.feature,
					new RandomPatchConfiguration(
						species.beehive.tries,
						16,
						2,
						PlacementUtils.onlyWhenEmpty(
							Feature.SIMPLE_BLOCK,
							new SimpleBlockConfiguration(
								BlockStateProvider.simple(species.beehive.block.get())
							)
						)
					)
				);

				var placedFeature = PlacementUtils.register(
					Beekeeping.get(species.beehive.getName() + "_placed").toString(),
					configuredFeature,
					RarityFilter.onAverageOnceEvery(species.beehive.rarity), InSquarePlacement.spread(), species.beehive.modifier, BiomeFilter.biome()
				);

				BIOME_MODIFICATION.add(
					ModificationPhase.ADDITIONS,
					selectionCtx -> species.beehive.acceptsBiome(selectionCtx.getBiomeRegistryEntry()),
					modificationCtx -> modificationCtx.getGenerationSettings().addBuiltInFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeature.value())
				);
			}
        }
		 */
    }
}
