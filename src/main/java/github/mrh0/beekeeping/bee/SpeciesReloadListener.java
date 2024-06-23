package github.mrh0.beekeeping.bee;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.RandomFunctionRegistry;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

public class SpeciesReloadListener {
    public static void register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return Beekeeping.get("species");
            }

            @Override
            public void onResourceManagerReload(@NotNull ResourceManager manager) {
                SpeciesRegistry.INSTANCE.clear();

                manager.listResources("beekeeping/species", id -> id.getPath().endsWith(".json")).forEach((id, resource) -> {
                    try (var reader = resource.openAsReader()) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        String name = json.get("name").getAsString();
                        int color = json.get("color").getAsInt();
                        boolean dark = json.get("dark").getAsBoolean();
                        boolean foil = json.get("foil").getAsBoolean();
                        boolean nocturnal = json.get("nocturnal").getAsBoolean();
                        BiomeTemperature temperature = BiomeTemperature.valueOf(json.get("preferred_temperature").getAsString().toUpperCase(Locale.ROOT)); // TODO: is there a better way to do this?

                        JsonObject jsonGeneSelectors = json.getAsJsonObject("gene_selectors");
                        Gene.RandomFunctions lifetimeGene = RandomFunctionRegistry.get(jsonGeneSelectors.get("lifetime").getAsString());
                        Gene.RandomFunctions lightToleranceGene = RandomFunctionRegistry.get(jsonGeneSelectors.get("light_tolerance").getAsString());
                        Gene.RandomFunctions rareProduceGene = RandomFunctionRegistry.get(jsonGeneSelectors.get("rare_produce").getAsString());
                        Gene.RandomFunctions temperatureToleranceGene = RandomFunctionRegistry.get(jsonGeneSelectors.get("temperature_tolerance").getAsString());
                        Gene.RandomFunctions weatherToleranceGene = RandomFunctionRegistry.get(jsonGeneSelectors.get("weather_tolerance").getAsString());

                        String parent1 = null;
                        String parent2 = null;
                        if (json.has("parents")) {
                            JsonArray jsonParents = json.getAsJsonArray("parents");
                            parent1 = jsonParents.get(0).getAsString();
                            parent2 = jsonParents.get(1).getAsString();
                        }

                        Produce.Builder produceBuilder = Produce.builder();

                        JsonObject jsonProduce = json.getAsJsonObject("produce");

                        JsonObject jsonProduceCommon = jsonProduce.getAsJsonObject("common");
                        Item commonItem = GsonHelper.getAsItem(jsonProduceCommon, "item");

                        JsonObject jsonProduceCommonCount = jsonProduceCommon.getAsJsonObject("count");
                        int commonCountUnsatisfied = jsonProduceCommonCount.get("unsatisfied").getAsInt();
                        int commonCountSatisfied = jsonProduceCommonCount.get("satisfied").getAsInt();

                        produceBuilder
                                .setCommonItem(commonItem)
                                .setCommonCount(commonCountUnsatisfied, commonCountSatisfied);

                        if (jsonProduce.has("rare")) {
                            JsonObject jsonProduceRare = jsonProduce.getAsJsonObject("rare");
                            Item rareItem = GsonHelper.getAsItem(jsonProduceRare, "item");

                            JsonObject jsonProduceRareCount = jsonProduceRare.getAsJsonObject("count");
                            int rareCountUnsatisfied = jsonProduceRareCount.get("unsatisfied").getAsInt();
                            int rareCountSatisfied = jsonProduceRareCount.get("satisfied").getAsInt();

                            JsonObject jsonProduceRareChance = jsonProduceRare.getAsJsonObject("chance");
                            double rareChanceUnsatisfied = jsonProduceRareChance.get("unsatisfied").getAsDouble();
                            double rareChanceSatisfied = jsonProduceRareChance.get("satisfied").getAsDouble();

                            produceBuilder
                                    .setRareItem(rareItem)
                                    .setRareCount(rareCountUnsatisfied, rareCountSatisfied)
                                    .setRareChance(rareChanceUnsatisfied, rareChanceSatisfied);
                        }

                        Species.Builder speciesBuilder = Species.builder(name);

                        speciesBuilder.setColor(color);
                        if (dark) speciesBuilder.setDark();
                        if (foil) speciesBuilder.setFoil();
                        if (nocturnal) speciesBuilder.setNocturnal();
                        speciesBuilder.setPreferredTemperature(temperature);

                        // TODO!!!
                        speciesBuilder.setLifetimeGene(lifetimeGene);
                        speciesBuilder.setLightGene(lightToleranceGene);
                        speciesBuilder.setProduceGene(rareProduceGene);
                        speciesBuilder.setTemperatureGene(temperatureToleranceGene);
                        speciesBuilder.setWeatherGene(weatherToleranceGene);

                        if (parent1 != null && parent2 != null) speciesBuilder.setParents(parent1, parent2);

                        speciesBuilder.setProduce(produceBuilder.build());

                        SpeciesRegistry.INSTANCE.register(speciesBuilder.build());
                    } catch (IOException e) {
                        Beekeeping.LOGGER.error("Failed to load species resource \""+id.toString()+"\"", e);
                    }
                });
            }
        });
    }
}
