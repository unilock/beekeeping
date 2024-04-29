package github.mrh0.beekeeping.blocks.analyzer;

import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.registry.ModBlockEntities;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class AnalyzerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    protected final ContainerData data;
    public AnalyzerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ANALYZER, pos, state);
        data = new SimpleContainerData(2);
    }

    public final SimpleContainer container = new SimpleContainer(1) {
        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            if (stack.getItem() instanceof BeeItem) {
                return super.canPlaceItem(index, stack);
            }

            return false;
        }

        @Override
        public void setChanged() {
            ItemStack stack = getAnalyzed();

            if (!stack.isEmpty() && stack.getItem() instanceof BeeItem && !BeeItem.getAnalyzed(stack)) {
                if (stack.getTag() == null) {
                    BeeItem.init(stack);
                }

                BeeItem.setAnalyzed(stack, true);
            }

            super.setChanged();

            AnalyzerBlockEntity.this.setChanged();
        }
    };

    public final InventoryStorage inventoryWrapper = InventoryStorage.of(container, null);

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beekeeping.analyzer");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new AnalyzerMenu(id, inv, this, this.data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", container.createTag());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        container.fromTag(nbt.getList("inventory", Tag.TAG_LIST));
    }

    public void drop() {
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    public ItemStack getAnalyzed() {
        return container.getItem(0);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.getBlockPos());
    }
}
