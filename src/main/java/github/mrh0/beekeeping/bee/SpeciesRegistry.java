package github.mrh0.beekeeping.bee;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpeciesRegistry {
    public static final SpeciesRegistry INSTANCE = new SpeciesRegistry();
    private final Map<String, Species> speciesMap = new HashMap<>();

    public Species register(Species species) {
        speciesMap.put(species.name, species);
        return species;
    }

    public Species get(String name) {
        return speciesMap.get(name);
    }

    public Collection<Species> getAll() {
        return speciesMap.values();
    }

    protected void clear() {
        speciesMap.clear();
    }
}
