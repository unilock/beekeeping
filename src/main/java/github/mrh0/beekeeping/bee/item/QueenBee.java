package github.mrh0.beekeeping.bee.item;

public class QueenBee extends BeeItem {
    public QueenBee(Properties props) {
        super(props);
    }

    @Override
    public BeeType getType() {
        return BeeType.QUEEN;
    }
}
