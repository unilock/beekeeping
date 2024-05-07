package github.mrh0.beekeeping.blocks.beehive;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveBlockEntity extends BlockEntity {
    public static final String SPECIES_KEY = "beekeeping:species";

    private Species species;

    public BeehiveBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BEEHIVE, pos, blockState);
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Species getSpecies() {
        return this.species;
    }

    @Override
    public void saveToItem(ItemStack stack) {
        super.saveToItem(stack);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.species = SpeciesRegistry.INSTANCE.get(tag.getString(SPECIES_KEY));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putString(SPECIES_KEY, this.species.name);
        super.saveAdditional(tag);
    }
}
