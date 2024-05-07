package github.mrh0.beekeeping.blocks.beehive;

import github.mrh0.beekeeping.bee.BeehiveRegistry;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BeehiveBlock extends Block {
    public static final SpeciesProperty SPECIES = SpeciesProperty.SPECIES;

    public BeehiveBlock() {
        super(Properties.copy(Blocks.BEE_NEST));
        this.registerDefaultState(this.stateDefinition.any().setValue(SPECIES, SpeciesRegistry.INSTANCE.get("common")));
    }

    public BlockState setSpecies(BlockState state, Species species) {
        return state.setValue(SPECIES, species);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPECIES);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        LootTable lootTable = beehiveLootTable(this, state.getValue(SPECIES));
        LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
        return lootTable.getRandomItems(lootParams);
    }

    private static LootTable beehiveLootTable(Block beehive, Species species) {
        ItemStack drone = species.getDrone();
        ItemStack princess = species.getPrincess();
        ItemStack queen = species.getQueen();

        assert drone.getTag() != null;
        assert princess.getTag() != null;
        assert queen.getTag() != null;

        return LootTable.lootTable()
                .withPool(LootPool.lootPool().when(BlockLootSubProvider.HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(beehive)))
                .withPool(LootPool.lootPool().when(BlockLootSubProvider.HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(princess.getItem()).apply(SetNbtFunction.setTag(princess.getTag())).setWeight(2))
                        .add(LootItem.lootTableItem(queen.getItem()).apply(SetNbtFunction.setTag(queen.getTag())).setWeight(1))
                )
                .withPool(LootPool.lootPool().when(BlockLootSubProvider.HAS_NO_SILK_TOUCH).setRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(princess.getItem()).apply(SetNbtFunction.setTag(princess.getTag())).setWeight(1))
                        .add(LootItem.lootTableItem(drone.getItem()).apply(SetNbtFunction.setTag(drone.getTag())).setWeight(3))
                )
                .build();
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (state.getBlock() == this) {
            return anySolid(state, level, pos);
        }

        return true;
    }

    private boolean anySolid(BlockState state, LevelReader level, BlockPos blockpos) {
        for (Direction dir : Direction.values()) {
            BlockPos pos = blockpos.relative(dir);

            if (level.getBlockState(pos).isFaceSturdy(level, pos, dir.getOpposite())) {
                return BeehiveRegistry.INSTANCE.getBeehive(state.getValue(SPECIES)).allowPlacement.apply(blockpos);
            }
        }

        return false;
    }
}
