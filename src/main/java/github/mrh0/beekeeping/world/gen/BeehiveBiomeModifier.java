package github.mrh0.beekeeping.world.gen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Beehive;
import github.mrh0.beekeeping.bee.BeehiveRegistry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BeehiveBiomeModifier {
    public static void modify() {
        for (Beehive beehive : BeehiveRegistry.INSTANCE.getAll()) {
            var id = Beekeeping.get(beehive.getName() + "_placed");

            Registry.register(BuiltInRegistries.FEATURE, id, beehive.feature);

            BiomeModifications.addFeature(
                    selectionCtx -> selectionCtx.hasTag(beehive.biomeTag),
                    GenerationStep.Decoration.VEGETAL_DECORATION,
                    ResourceKey.create(Registries.PLACED_FEATURE, id)
            );
        }
    }
}
