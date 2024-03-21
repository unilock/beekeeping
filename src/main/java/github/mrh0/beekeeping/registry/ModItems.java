package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.item.ThermometerItem;
import github.mrh0.beekeeping.item.frame.FrameItem;
import github.mrh0.beekeeping.item.frame.ProduceEvent;
import github.mrh0.beekeeping.item.frame.SatisfactionEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final ThermometerItem THERMOMETER = register("thermometer", new ThermometerItem());
    public static final FrameItem BASIC_FRAME = register("basic_frame", new FrameItem("basic"));
    public static final FrameItem GLOWING_FRAME = register("glowing_frame", new FrameItem("glowing").addSatisfactionEvent((level, pos, type, queen, satisfaction) -> type == SatisfactionEvent.SatisfactionType.LIGHT ? satisfaction.up() : satisfaction));
    public static final FrameItem INSULATED_FRAME = register("insulated_frame", new FrameItem("insulated").addSatisfactionEvent((level, pos, type, queen, satisfaction) -> type == SatisfactionEvent.SatisfactionType.TEMPERATURE ? satisfaction.up() : satisfaction));
    public static final FrameItem MUTATION_FRAME = register("mutation_frame", new FrameItem("mutation").addProduceEvent((level, pos, type, stack) -> type == ProduceEvent.ProduceType.DRONE || type == ProduceEvent.ProduceType.PRINCESS ? BeeLifecycle.mutateRandom(stack) : stack));
    public static final FrameItem WATERPROOF_FRAME = register("waterproof_frame", new FrameItem("waterproof").addSatisfactionEvent((level, pos, type, queen, satisfaction) -> type == SatisfactionEvent.SatisfactionType.WEATHER ? satisfaction.up() : satisfaction));

    public static void init() {
        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
            register(species.getName() + "_drone", species.buildDroneItem());
            register(species.getName() + "_princess", species.buildPrincessItem());
            register(species.getName() + "_queen", species.buildQueenItem());
        }
    }

    private static <T extends Item> T register(String path, T item) {
        Registry.register(BuiltInRegistries.ITEM, Beekeeping.get(path), item);
        return item;
    }
}
