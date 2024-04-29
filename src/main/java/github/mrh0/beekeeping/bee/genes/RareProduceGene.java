package github.mrh0.beekeeping.bee.genes;

import net.minecraft.world.item.ItemStack;

public enum RareProduceGene implements Gene {
    HIGHEST("highest"),
    HIGH("high"),
    NORMAL("normal"),
    LOW("low"),
    LOWEST("lowest");

    private final String name;

    RareProduceGene(String name) {
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
        Gene.set(stack, "prod", value);
    }

    public static int get(ItemStack stack) {
        return Gene.get(stack, "prod");
    }

    public static RareProduceGene of(int value) {
        return switch (value) {
            case 0 -> LOWEST;
            case 1 -> LOW;
            case 3 -> HIGH;
            case 4 -> HIGHEST;
            default -> NORMAL;
        };
    }

    public double getChance() {
        return switch(this) {
            case LOWEST -> 0.5d;
            case LOW -> 0.7d;
            case HIGH -> 1.3d;
            case HIGHEST -> 1.5d;
            default -> 1d;
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
