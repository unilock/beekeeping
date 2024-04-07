package github.mrh0.beekeeping.world.gen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BeehiveBiomeModifier {
    public static void modify() {
        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if (species.hasBeehive()) {
                var id = Beekeeping.get(species.beehive.getName() + "_placed");

                Registry.register(BuiltInRegistries.FEATURE, id, species.beehive.feature);

                BiomeModifications.addFeature(
                        selectionCtx -> species.beehive.acceptsBiome(selectionCtx.getBiomeRegistryEntry()),
                        GenerationStep.Decoration.VEGETAL_DECORATION,
                        ResourceKey.create(Registries.PLACED_FEATURE, id)
                );
            }
        }
    }
}
