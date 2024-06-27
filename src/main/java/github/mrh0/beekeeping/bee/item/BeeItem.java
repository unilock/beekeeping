package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.bee.genes.LightToleranceGene;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.genes.TemperatureToleranceGene;
import github.mrh0.beekeeping.bee.genes.WeatherToleranceGene;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BeeItem extends Item {
    public static final String ANALYZED_KEY = "beekeeping:analyzed";
    public static final String GENES_KEY = "beekeeping:genes";
    public static final String HEALTH_KEY = "beekeeping:health";
    public static final String SPECIES_KEY = "beekeeping:species";

    public BeeItem(Properties props) {
        super(props);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (!(stack.getItem() instanceof BeeItem)) {
            return false;
        }

        return getSpecies(stack) != null && getSpecies(stack).foil;
    }

    public abstract BeeType getType();

    public static void setAnalyzed(ItemStack stack, boolean analyzed) {
        if (!(stack.getItem() instanceof BeeItem)) {
            throw new IllegalArgumentException("[Beekeeping] setAnalyzed called on invalid stack");
        }

        stack.getOrCreateTag().putBoolean(ANALYZED_KEY, analyzed);
    }

    public static boolean getAnalyzed(ItemStack stack) {
        if (!(stack.getItem() instanceof BeeItem)) {
            return false;
        }

        return stack.getOrCreateTag().getBoolean(ANALYZED_KEY);
    }

    public static void setHealth(ItemStack stack, int health) {
        if (!(stack.getItem() instanceof BeeItem)) {
            throw new IllegalArgumentException("[Beekeeping] setHealth called on invalid stack");
        }

        stack.getOrCreateTag().putInt(HEALTH_KEY, health);
    }

    public static int getHealth(ItemStack stack) {
        if (!(stack.getItem() instanceof BeeItem)) {
            return 0;
        }

        return stack.getOrCreateTag().getInt(HEALTH_KEY);
    }

    public static void setSpecies(ItemStack stack, Species species) {
        if (!(stack.getItem() instanceof BeeItem)) {
            throw new IllegalArgumentException("[Beekeeping] setSpecies called on invalid stack");
        }

        stack.getOrCreateTag().putString(SPECIES_KEY, species.name);
    }

    public static Species getSpecies(ItemStack stack) {
        if (!(stack.getItem() instanceof BeeItem) || stack.getTag() == null) {
            return null;
        }

        return SpeciesRegistry.INSTANCE.get(stack.getTag().getString(SPECIES_KEY));
    }

    public static void init(ItemStack stack) {
        if (!(stack.getItem() instanceof BeeItem)) {
            return;
        }

        Species species = getSpecies(stack);

        if (species == null) {
            throw new IllegalArgumentException("[Beekeeping] init called on invalid stack");
        }

        init(
                stack,
                Gene.eval(species.lifetimeGene.func),
                Gene.eval(species.weatherGene.func),
                Gene.eval(species.temperatureGene.func),
                Gene.eval(species.lightGene.func),
                Gene.eval(species.produceGene.func)
        );

    }

    public static void init(ItemStack stack, int lifetimeGene, int weatherGene, int temperatureGene, int lightGene, int produceGene) {
        setAnalyzed(stack, false);

        //System.out.println(lifetimeGene + ":" + weatherGene + ":" + temperatureGene + ":" + lightGene + ":" + produceGene);
        //System.out.println(LifetimeGene.of(lifetimeGene).getName() + ":" + WeatherToleranceGene.of(biomeGene).getName() + ":" + LightToleranceGene.of(lightGene).getName() + ":" + RareProduceGene.of(produceGene).getName());

        LifetimeGene.set(stack, lifetimeGene);
        LightToleranceGene.set(stack, lightGene);
        RareProduceGene.set(stack, produceGene);
        TemperatureToleranceGene.set(stack, temperatureGene);
        WeatherToleranceGene.set(stack, weatherGene);

        setHealth(stack, LifetimeGene.of(LifetimeGene.get(stack)).getTime());
    }

    /*
    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(stack.getTag() == null)
            return false;
        return getAnalyzed(stack.getTag());
    }
    */

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (getHealthOf(stack) * 13.0);
    }

    public static double getHealthOf(ItemStack stack) {
        if (stack.getTag() == null) {
            return 1.0;
        }

        double health = getHealth(stack);
        double max = LifetimeGene.of(LifetimeGene.get(stack)).getTime();
        return health/max;
    }

    @Override
    public Component getName(ItemStack stack) {
        String name;
        if (!(stack.getItem() instanceof BeeItem) || getSpecies(stack) == null) {
            name = "null";
        } else {
            name = StringUtils.capitalize(getSpecies(stack).name);
        }

        return Component.translatable(this.getDescriptionId(stack), name);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);

        if (!getAnalyzed(stack)) {
            list.add(Component.translatable("tooltip.beekeeping.gene.not_analyzed").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            return;
        }

        list.add(Component.translatable("tooltip.beekeeping.gene.lifetime")
                .append(": ").append(LifetimeGene.of(LifetimeGene.get(stack)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.weather")
                .append(": ").append(WeatherToleranceGene.of(WeatherToleranceGene.get(stack)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.temperature")
                .append(": ").append(TemperatureToleranceGene.of(TemperatureToleranceGene.get(stack)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.light")
                .append(": ").append(LightToleranceGene.of(LightToleranceGene.get(stack)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.produce")
                .append(": ").append(RareProduceGene.of(RareProduceGene.get(stack)).getComponent()));

        //list.add(Component.literal("health: " + getHealth(stack.getTag())));
    }

    public static boolean is(ItemStack stack, Species species) {
        return getSpecies(stack) == species;
    }

    public enum BeeType {
        DRONE("drone"),
        PRINCESS("princess"),
        QUEEN("queen");

        public final String name;

        BeeType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
