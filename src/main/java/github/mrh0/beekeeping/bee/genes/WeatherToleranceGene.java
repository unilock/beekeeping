package github.mrh0.beekeeping.bee.genes;

import net.minecraft.world.item.ItemStack;

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

    @Override
    public int getIndex() {
        return ordinal();
    }

    public static void set(ItemStack stack, int value) {
        Gene.set(stack, "weather", value);
    }

    public static int get(ItemStack stack) {
        return Gene.get(stack, "weather");
    }

    public static WeatherToleranceGene of(int value) {
        return switch (value) {
            case 0 -> STRICT;
            case 1 -> PICKY;
            default -> ANY;
        };
    }

    public static ItemStack up(ItemStack bee) {
        int now = get(bee);
        set(bee, Math.min(now+1, 4));
        return bee;
    }

    public static ItemStack down(ItemStack bee) {
        int now = get(bee);
        set(bee, Math.max(now+1, 0));
        return bee;
    }
}
