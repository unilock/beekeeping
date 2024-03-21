package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import net.minecraft.resources.ResourceLocation;

public class DroneBee extends BeeItem {
    private final ResourceLocation resource;
    public DroneBee(Species species, Properties props, boolean foil) {
        super(species, props, foil);
        resource = new ResourceLocation(Beekeeping.MODID, species.getName() + "_drone");
    }

    @Override
    public BeeType getType() {
        return BeeType.DRONE;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return resource;
    }
}
