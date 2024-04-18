package github.mrh0.beekeeping.bee;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeehiveRegistry {
    public static final BeehiveRegistry INSTANCE = new BeehiveRegistry();
    private final Map<Species, Beehive> beehiveMap = new HashMap<>();

    public Beehive register(Beehive beehive) {
        beehiveMap.put(beehive.species, beehive);
        return beehive;
    }

    public Beehive getBeehive(Species species) {
        return beehiveMap.get(species);
    }

    public Collection<Beehive> getAll() {
        return beehiveMap.values();
    }

    protected void clear() {
        beehiveMap.clear();
    }
}
