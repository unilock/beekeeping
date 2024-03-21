package github.mrh0.beekeeping.bee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesRegistry {
    public static final SpeciesRegistry INSTANCE = new SpeciesRegistry();
    private final List<Species> speciesList = new ArrayList<>();
    private final Map<String, Species> speciesMap = new HashMap<>();

    public Species register(Species species) {
        speciesList.add(species);
        speciesMap.put(species.getName(), species);
        return species;
    }

    public Species get(String name) {
        return speciesMap.get(name);
    }

    public Species get(int index) {
        return speciesList.get(index);
    }

    public List<Species> getAll() {
        return speciesList;
    }
}
