package github.mrh0.beekeeping.bee;

import com.mojang.datafixers.util.Pair;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.LightToleranceGene;
import github.mrh0.beekeeping.bee.genes.TemperatureToleranceGene;
import github.mrh0.beekeeping.bee.genes.WeatherToleranceGene;
import github.mrh0.beekeeping.bee.item.DroneBee;
import github.mrh0.beekeeping.bee.item.PrincessBee;
import github.mrh0.beekeeping.bee.item.QueenBee;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.config.Config;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Species {
    public final String name;
    public final int color;
    public final boolean dark;
    public final boolean foil;
    public final boolean nocturnal;
    public final BiomeTemperature preferredTemperature;
    public final Gene.RandomFunction lifetimeGene;
    public final Gene.RandomFunction lightGene;
    public final Gene.RandomFunction produceGene;
    public final Gene.RandomFunction temperatureGene;
    public final Gene.RandomFunction weatherGene;
    public final Produce produce;
    public final Pair<String, String> parents;

    public final DroneBee droneItem;
    public final PrincessBee princessItem;
    public final QueenBee queenItem;

    private Species(String name, int color, boolean dark, boolean foil, boolean nocturnal, BiomeTemperature preferredTemperature, Gene.RandomFunction lifetimeGene, Gene.RandomFunction lightGene, Gene.RandomFunction produceGene, Gene.RandomFunction temperatureGene, Gene.RandomFunction weatherGene, Produce produce, Pair<String, String> parents) {
        this.name = name;
        this.color = color;
        this.dark = dark;
        this.foil = foil;
        this.nocturnal = nocturnal;
        this.preferredTemperature = preferredTemperature;
        this.lifetimeGene = lifetimeGene;
        this.lightGene = lightGene;
        this.produceGene = produceGene;
        this.temperatureGene = temperatureGene;
        this.weatherGene = weatherGene;
        this.produce = produce;
        this.parents = parents;

        this.droneItem = new DroneBee(this, new FabricItemSettings().stacksTo(1), foil);
        this.princessItem = new PrincessBee(this, new FabricItemSettings().stacksTo(1), foil);
        this.queenItem = new QueenBee(this, new FabricItemSettings().stacksTo(1), foil);
    }

    public boolean hasSatisfactoryLightLevel(int sunlight, boolean isDay) {
        int limit = Config.LIGHT_GENE_MIN_LIGHT.get();

        if (nocturnal) {
            return sunlight > limit && !isDay;
        } else {
            return sunlight > limit && isDay;
        }
    }

    public Satisfaction getLightSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        if (Config.IGNORE_LIGHT_SATISFACTION.get()) {
            return Satisfaction.SATISFIED;
        }

        int sunlight = Util.getSunlight(level, pos);
        LightToleranceGene tolerance = LightToleranceGene.of(LightToleranceGene.get(stack.getTag()));

        return switch (tolerance) {
            case PICKY -> hasSatisfactoryLightLevel(sunlight, level.isDay()) ? Satisfaction.SATISFIED : Satisfaction.UNSATISFIED;
            case STRICT -> hasSatisfactoryLightLevel(sunlight, level.isDay()) ? Satisfaction.SATISFIED : Satisfaction.NOT_WORKING;
            default -> Satisfaction.SATISFIED;
        };
    }

    public Satisfaction getTemperatureSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        if(Config.IGNORE_TEMPERATURE_SATISFACTION.get()) {
            return Satisfaction.SATISFIED;
        }

        BiomeTemperature temp = BiomeTemperature.of(level.getBiomeManager().getBiome(pos).value().getBaseTemperature());
        TemperatureToleranceGene tolerance = TemperatureToleranceGene.of(TemperatureToleranceGene.get(stack.getTag()));

        return switch (tolerance) {
            case PICKY -> preferredTemperature == temp ? Satisfaction.SATISFIED : (preferredTemperature.isAdjacent(temp) ? Satisfaction.UNSATISFIED : Satisfaction.NOT_WORKING);
            case STRICT -> preferredTemperature == temp ? Satisfaction.SATISFIED : Satisfaction.NOT_WORKING;
            default -> Satisfaction.SATISFIED;
        };
    }

    public Satisfaction getWeatherSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        if (Config.IGNORE_WEATHER_SATISFACTION.get()) {
            return Satisfaction.SATISFIED;
        }

        if (level.isThundering()) {
            return Satisfaction.NOT_WORKING;
        }

        WeatherToleranceGene tolerance = WeatherToleranceGene.of(WeatherToleranceGene.get(stack.getTag()));

        return switch (tolerance) {
            case PICKY -> level.isRaining() ? Satisfaction.UNSATISFIED : Satisfaction.SATISFIED;
            case STRICT -> level.isRaining() ? Satisfaction.NOT_WORKING : Satisfaction.SATISFIED;
            default -> Satisfaction.SATISFIED;
        };
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        final String name;
        int color = 0x00000000;
        boolean dark = false;
        boolean foil = false;
        boolean nocturnal = false;
        BiomeTemperature preferredTemperature = BiomeTemperature.TEMPERED;
        Gene.RandomFunction lifetimeGene = Gene::random5Narrow;
        Gene.RandomFunction lightGene = Gene::strict;
        Gene.RandomFunction produceGene = Gene::random5Narrow;
        Gene.RandomFunction temperatureGene = Gene::picky;
        Gene.RandomFunction weatherGene = Gene::strict;
        Produce produce;
        Pair<String, String> parents;

        public Builder(String name) {
            this.name = name;
        }

        public Species build() {
            if (this.produce == null) {
                throw new IllegalArgumentException("Species produce cannot be null!");
            }

            return new Species(this.name, this.color, this.dark, this.foil, this.nocturnal, this.preferredTemperature, this.lifetimeGene, this.lightGene, this.produceGene, this.temperatureGene, this.weatherGene, this.produce, this.parents);
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setDark() {
            this.dark = true;
            return this;
        }

        public Builder setFoil() {
            this.foil = true;
            return this;
        }

        public Builder setNocturnal() {
            this.nocturnal = true;
            return this;
        }

        public Builder setPreferredTemperature(BiomeTemperature temperature) {
            this.preferredTemperature = temperature;
            return this;
        }

        public Builder setLifetimeGene(Gene.RandomFunction func) {
            this.lifetimeGene = func;
            return this;
        }

        public Builder setLightGene(Gene.RandomFunction func) {
            this.lightGene = func;
            return this;
        }

        public Builder setProduceGene(Gene.RandomFunction func) {
            this.produceGene = func;
            return this;
        }

        public Builder setTemperatureGene(Gene.RandomFunction func) {
            this.temperatureGene = func;
            return this;
        }

        public Builder setWeatherGene(Gene.RandomFunction func) {
            this.weatherGene = func;
            return this;
        }

        public Builder setProduce(Produce produce) {
            this.produce = produce;
            return this;
        }

        public Builder setParents(String first, String second) {
            this.parents = new Pair<>(first, second);
            return this;
        }
    }
}
