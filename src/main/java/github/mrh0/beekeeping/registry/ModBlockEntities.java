package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.blocks.beehive.BeehiveBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("UnstableApiUsage")
public class ModBlockEntities {
    public static final BlockEntityType<AnalyzerBlockEntity> ANALYZER = register("analyzer", AnalyzerBlockEntity::new, ModBlocks.ANALYZER);
    public static final BlockEntityType<ApiaryBlockEntity> APIARY = register("apiary", ApiaryBlockEntity::new, ModBlocks.APIARY);
    public static final BlockEntityType<BeehiveBlockEntity> BEEHIVE = register("beehive", BeehiveBlockEntity::new, ModBlocks.BEEHIVE);

    public static void init() {
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventoryWrapper, ANALYZER);
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> Direction.UP.equals(direction) ? blockEntity.inputInventoryWrapper : blockEntity.outputInventoryWrapper, APIARY);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Beekeeping.get(path), FabricBlockEntityTypeBuilder.create(factory).addBlocks(blocks).build());
    }
}
