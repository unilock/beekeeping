package github.mrh0.beekeeping.blocks.beehive;

import github.mrh0.beekeeping.bee.Beehive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveBlock extends Block {
    private final Beehive beehive;

    public BeehiveBlock(Beehive beehive) {
        super(Properties.copy(Blocks.BEE_NEST));
        this.beehive = beehive;
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
                return this.beehive.allowPlacement.apply(blockpos);
            }
        }

        return false;
    }
}
