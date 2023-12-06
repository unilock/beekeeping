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
            if (!species.hasBeehive()) {
                continue;
            }

			BIOME_MODIFICATION.add(
				ModificationPhase.ADDITIONS,
				selectionCtx -> species.beehive.acceptsBiome(selectionCtx.getBiomeRegistryEntry()),
				modificationCtx -> modificationCtx.getGenerationSettings().addBuiltInFeature(Decoration.VEGETAL_DECORATION, BeekeepingPlacedFeatures.getPlacedFeatures(species.beehive, species.beehive.rarity, species.beehive.modifier, species.beehive.feature).value())
			);
        }
		 */
    }
}
