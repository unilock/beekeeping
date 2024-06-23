package github.mrh0.beekeeping.bee.genes;

import java.util.HashMap;
import java.util.Map;

public class RandomFunctionRegistry {
    public static final Map<String, Gene.RandomFunctions> MAP = new HashMap<>();

    public static void register(Gene.RandomFunctions func) {
        MAP.put(func.name, func);
    }

    public static Gene.RandomFunctions get(String name) {
        return MAP.get(name);
    }
}
