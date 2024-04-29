package github.mrh0.beekeeping.bee.item;

public class PrincessBee extends BeeItem {
    public PrincessBee(Properties props) {
        super(props);
    }

    @Override
    public BeeType getType() {
        return BeeType.PRINCESS;
    }
}
