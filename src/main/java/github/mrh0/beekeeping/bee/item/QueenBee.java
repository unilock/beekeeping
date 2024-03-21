package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Species;
import net.minecraft.resources.ResourceLocation;

public class QueenBee extends BeeItem {
    private final ResourceLocation resource;
    public QueenBee(Species species, Properties props, boolean foil) {
        super(species, props, foil);
        resource = new ResourceLocation(species.mod, species.getName() + "_drone");
    }

    @Override
    public BeeType getType() {
        return BeeType.QUEEN;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return resource;
    }
}
