package github.mrh0.beekeeping.bee;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record Produce(
        Item common,
        int commonCountUnsatisfied,
        int commonCountSatisfied,
        Item rare,
        int rareCountUnsatisfied,
        int rareCountSatisfied,
        double rareChanceUnsatisfied,
        double rareChanceSatisfied
) {
    public boolean isRare() {
        return this.rare != Items.AIR;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        Item common = Items.AIR;
        int commonCountUnsatisfied = 0;
        int commonCountSatisfied = 0;
        Item rare = Items.AIR;
        int rareCountUnsatisfied = 0;
        int rareCountSatisfied = 0;
        double rareChanceUnsatisfied = 1.0;
        double rareChanceSatisfied = 1.0;

        public Produce build() {
            return new Produce(this.common, this.commonCountUnsatisfied, this.commonCountSatisfied, this.rare, this.rareCountUnsatisfied, this.rareCountSatisfied, this.rareChanceUnsatisfied, this.rareChanceSatisfied);
        }

        public Builder setCommonItem(Item common) {
            this.common = common;
            return this;
        }

        public Builder setCommonCount(int unsatisfied, int satisfied) {
            this.commonCountUnsatisfied = unsatisfied;
            this.commonCountSatisfied = satisfied;
            return this;
        }

        public Builder setRareItem(Item rare) {
            this.rare = rare;
            return this;
        }

        public Builder setRareCount(int unsatisfied, int satisfied) {
            this.rareCountUnsatisfied = unsatisfied;
            this.rareCountSatisfied = satisfied;
            return this;
        }

        public Builder setRareChance(double unsatisfied, double satisfied) {
            this.rareChanceUnsatisfied = unsatisfied;
            this.rareChanceSatisfied = satisfied;
            return this;
        }
    }
}
