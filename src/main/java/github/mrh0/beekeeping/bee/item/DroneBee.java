package github.mrh0.beekeeping.bee.item;

public class DroneBee extends BeeItem {
    public DroneBee(Properties props) {
        super(props);
    }

    @Override
    public BeeType getType() {
        return BeeType.DRONE;
    }
}
