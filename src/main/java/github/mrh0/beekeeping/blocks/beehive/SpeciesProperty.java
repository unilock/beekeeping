package github.mrh0.beekeeping.blocks.beehive;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class SpeciesProperty extends Property<Species> {
    public static final SpeciesProperty SPECIES = new SpeciesProperty("species");

    protected SpeciesProperty(String name) {
        super(name, Species.class);
    }

    @Override
    @NotNull
    public Collection<Species> getPossibleValues() {
        return SpeciesRegistry.INSTANCE.getAll();
    }

    @Override
    @NotNull
    public String getName(Species value) {
        return value.name;
    }

    @Override
    @NotNull
    public Optional<Species> getValue(@NotNull String value) {
        return Optional.ofNullable(SpeciesRegistry.INSTANCE.get(value));
    }
}
