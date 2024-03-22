package github.mrh0.beekeeping.blocks.beehive;

import github.mrh0.beekeeping.bee.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveBlock extends Block {
    private final Species species;

    public BeehiveBlock(Species species) {
        super(Properties.copy(Blocks.BEE_NEST));
        this.species = species;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() == this) {
            return anySolid(level, pos);
        }

        return true;
    }

    private boolean anySolid(LevelReader level, BlockPos blockpos) {
        for (Direction dir : Direction.values()) {
            BlockPos pos = blockpos.relative(dir);

            if (level.getBlockState(pos).isFaceSturdy(level, pos, dir.getOpposite())) {
                return species.beehive.blockPlaceAllow.apply(blockpos);
            }
        }

        return false;
    }
}
