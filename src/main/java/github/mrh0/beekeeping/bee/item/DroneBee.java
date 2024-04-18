package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Species;

public class DroneBee extends BeeItem {
    public DroneBee(Species species, Properties props, boolean foil) {
        super(species, props, foil);
    }

    @Override
    public BeeType getType() {
        return BeeType.DRONE;
    }
}
