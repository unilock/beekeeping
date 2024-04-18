package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.bee.genes.LightToleranceGene;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.genes.TemperatureToleranceGene;
import github.mrh0.beekeeping.bee.genes.WeatherToleranceGene;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BeeItem extends Item {
    private final Species species;
    private final boolean foil;

    public BeeItem(Species species, Properties props, boolean foil) {
        super(props);
        this.species = species;
        this.foil = foil;
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

    @Override
    public boolean isFoil(ItemStack item) {
        return foil;
    }

    public abstract BeeType getType();

    public static void setAnalyzed(CompoundTag tag, boolean analyzed) {
        tag.putBoolean("analyzed", analyzed);
    }

    public static boolean getAnalyzed(CompoundTag tag) {
        return tag.getBoolean("analyzed");
    }

    public static boolean isAnalyzed(ItemStack stack) {
        if(stack.isEmpty())
            return false;
        if(!(stack.getItem() instanceof BeeItem))
            return false;
        if(stack.getTag() == null)
            return false;
        return getAnalyzed(stack.getTag());
    }

    public static void setHealth(CompoundTag tag, int health) {
        tag.putInt("health", health);
    }

    public static int getHealth(CompoundTag tag) {
        return tag.getInt("health");
    }

    public static void init(ItemStack stack) {
        if(stack.isEmpty())
            return;
        if(!(stack.getItem() instanceof BeeItem))
            return;
        if(stack.getTag() == null)
            stack.setTag(new CompoundTag());
        CompoundTag tag = stack.getTag();
        if(!(stack.getItem() instanceof BeeItem))
            return;
        BeeItem beeItem = (BeeItem) stack.getItem();

        init(tag, beeItem,
                Gene.eval(beeItem.species.lifetimeGene),
                Gene.eval(beeItem.species.weatherGene),
                Gene.eval(beeItem.species.temperatureGene),
                Gene.eval(beeItem.species.lightGene),
                Gene.eval(beeItem.species.produceGene)
        );

    }

    public static void init(CompoundTag tag, BeeItem beeItem, int lifetimeGene, int weatherGene, int temperatureGene, int lightGene, int produceGene) {
        setAnalyzed(tag, false);

        //System.out.println(lifetimeGene + ":" + weatherGene + ":" + temperatureGene + ":" + lightGene + ":" + produceGene);
        //System.out.println(LifetimeGene.of(lifetimeGene).getName() + ":" + WeatherToleranceGene.of(biomeGene).getName() + ":" + LightToleranceGene.of(lightGene).getName() + ":" + RareProduceGene.of(produceGene).getName());

        LifetimeGene.set(tag, lifetimeGene);
        WeatherToleranceGene.set(tag, weatherGene);
        TemperatureToleranceGene.set(tag, temperatureGene);
        LightToleranceGene.set(tag, lightGene);
        RareProduceGene.set(tag, produceGene);

        setHealth(tag, LifetimeGene.of(LifetimeGene.get(tag)).getTime());
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
        return (int) (getHealthOf(stack) * 13d);
    }

    public static double getHealthOf(ItemStack stack) {
        if(stack.getTag() == null)
            return 1d;
        double health = getHealth(stack.getTag());
        double max = LifetimeGene.of(LifetimeGene.get(stack.getTag())).getTime();
        return health/max;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if(stack.isEmpty())
            return;
        if(!(stack.getItem() instanceof BeeItem))
            return;
        if(stack.getTag() == null || !isAnalyzed(stack)) {
            list.add(Component.translatable("tooltip.beekeeping.gene.not_analyzed").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            return;
        }

        CompoundTag tag = stack.getTag();
        list.add(Component.translatable("tooltip.beekeeping.gene.lifetime")
                .append(": ").append(LifetimeGene.of(LifetimeGene.get(tag)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.weather")
                .append(": ").append(WeatherToleranceGene.of(WeatherToleranceGene.get(tag)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.temperature")
                .append(": ").append(TemperatureToleranceGene.of(TemperatureToleranceGene.get(tag)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.light")
                .append(": ").append(LightToleranceGene.of(LightToleranceGene.get(tag)).getComponent()));
        list.add(Component.translatable("tooltip.beekeeping.gene.produce")
                .append(": ").append(RareProduceGene.of(RareProduceGene.get(tag)).getComponent()));

        //list.add(Component.literal("health: " + getHealth(stack.getTag())));
    }

    public static Species speciesOf(ItemStack stack) {
        if(stack == null)
            return null;
        if(stack.isEmpty())
            return null;
        if(!(stack.getItem() instanceof BeeItem))
            return null;
        return ((BeeItem) stack.getItem()).species;
    }

    public static boolean is(ItemStack stack, Species species) {
        return speciesOf(stack) == species;
    }
}
