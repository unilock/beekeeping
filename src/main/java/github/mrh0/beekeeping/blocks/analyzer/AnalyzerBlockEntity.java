package github.mrh0.beekeeping.blocks.analyzer;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
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

public class AnalyzerBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    public AnalyzerBlockEntity(BlockPos pos, BlockState state) {
        super(Index.ANALYZER_BLOCK_ENTITY.get(), pos, state);
        data = new SimpleContainerData(2);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            if(slot == 0) {
                ItemStack stack = getAnalyzed();
                if(stack.isEmpty())
                    return;
                if(!(stack.getItem() instanceof BeeItem))
                    return;
                if(BeeItem.isAnalyzed(stack))
                    return;
                if(stack.getTag() == null)
                    BeeItem.init(stack);
                BeeItem.setAnalyzed(stack.getTag(), true);
            }
            setChanged();
        }
    };

    public LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beekeeping.analyzer");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new AnalyzerMenu(id, inv, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drop() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStack getAnalyzed() {
        return itemHandler.getStackInSlot(0);
    }
}
