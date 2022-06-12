package github.mrh0.beekeeping.bee.genes;

import net.minecraft.nbt.CompoundTag;

public enum WeatherToleranceGene implements Gene {
    STRICT("strict"),
    PICKY("picky"),
    ANY("any");

    private final String name;

    WeatherToleranceGene(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static void set(CompoundTag tag, int value) {
        Gene.set(tag, "weather", value);
    }

    public static int get(CompoundTag tag) {
        return Gene.get(tag, "weather");
    }

    public static WeatherToleranceGene of(int value) {
        return switch (value) {
            case 1 -> STRICT;
            case 3 -> PICKY;
            default -> ANY;
        };
    }
}