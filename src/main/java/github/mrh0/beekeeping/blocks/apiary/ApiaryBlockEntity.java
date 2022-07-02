package github.mrh0.beekeeping.blocks.apiary;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Satisfaction;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.network.IHasToggleOption;
import github.mrh0.beekeeping.network.TogglePacket;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ApiaryBlockEntity extends BlockEntity implements MenuProvider, IHasToggleOption {
    protected final ContainerData data;
    public ApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(Index.APIARY_BLOCK_ENTITY.get(), pos, state);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> breedProgressTime;
                    case 1 -> satisfactionCache == null ? 0 : satisfactionCache.ordinal();
                    case 2 -> weatherSatisfactionCache == null ? 0 : weatherSatisfactionCache.ordinal();
                    case 3 -> temperatureSatisfactionCache == null ? 0 : temperatureSatisfactionCache.ordinal();
                    case 4 -> lightSatisfactionCache == null ? 0 : lightSatisfactionCache.ordinal();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0: breedProgressTime = value;
                        break;
                    case 1: satisfactionCache = Satisfaction.of(value);
                        break;
                    case 2: weatherSatisfactionCache = Satisfaction.of(value);
                        break;
                    case 3: temperatureSatisfactionCache = Satisfaction.of(value);
                        break;
                    case 4: lightSatisfactionCache = Satisfaction.of(value);
                        break;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    public boolean continuous = false;
    public int breedProgressTime = 0;
    public static final int BREED_TIME = 60;
    private Specie offspringCache = null;

    public ItemStack getDrone() {
        return itemHandler.getStackInSlot(0);
    }

    public ItemStack getPrincess() {
        return itemHandler.getStackInSlot(1);
    }

    public ItemStack getQueen() {
        return itemHandler.getStackInSlot(2);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            checkLock = false;
            if(slot < 3) {
                if(getLevel().isClientSide())
                    return;
                breedCheck();
            }
            setChanged();
        }
    };

    private void breedCheck() {
        ItemStack drone = getDrone();
        ItemStack princess = getPrincess();
        ItemStack queen = getQueen();

        if(drone.isEmpty())
            return;
        if(princess.isEmpty())
            return;
        if(!queen.isEmpty())
            return;

        if(drone.getTag() == null)
            BeeItem.init(drone);
        if(princess.getTag() == null)
            BeeItem.init(princess);

        offspringCache = BeeLifecycle.getOffspringSpecie(getLevel(), drone, princess);
    }

    private void preformBreeding() {
        if(offspringCache == null)
            return;

        ItemStack offspringQueen = new ItemStack(offspringCache.queenItem);
        offspringQueen.setTag(BeeLifecycle.getOffspringItemStack(getDrone(), getPrincess(), offspringCache));
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        itemHandler.setStackInSlot(1, ItemStack.EMPTY);
        itemHandler.setStackInSlot(2, offspringQueen);
    }

    public void updateSatisfaction() {
        if(getQueen() == null || getQueen().isEmpty()) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        Specie specie = BeeItem.of(getQueen());
        if(specie == null) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        lightSatisfactionCache = specie.getLightSatisfaction(getQueen(), getLevel(), getBlockPos());
        weatherSatisfactionCache = specie.getWeatherSatisfaction(getQueen(), getLevel(), getBlockPos());
        temperatureSatisfactionCache = specie.getTemperatureSatisfaction(getQueen(), getLevel(), getBlockPos());

        satisfactionCache = Satisfaction.calc(lightSatisfactionCache, weatherSatisfactionCache, temperatureSatisfactionCache);
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.beekeeping.apiary");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ApiaryMenu(id, inv, this, this.data);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyItemHandler.cast();

        return super.getCapability(cap, side);
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
        tag.putBoolean("continuous", continuous);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        continuous = nbt.getBoolean("continuous");
    }

    public void drop() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public int slowTick = 0;
    public boolean checkLock = false;
    public Satisfaction satisfactionCache;
    public Satisfaction weatherSatisfactionCache;
    public Satisfaction temperatureSatisfactionCache;
    public Satisfaction lightSatisfactionCache;
    public static void tick(Level level, BlockPos pos, BlockState state, ApiaryBlockEntity abe) {
        abe.localTick();
    }

    public void localTick() {
        if(getLevel().isClientSide())
            return;

        if(getQueen().isEmpty() && !getDrone().isEmpty() && !getPrincess().isEmpty()) {
            breedProgressTime++;
            if(breedProgressTime > BREED_TIME) {
                breedProgressTime = 0;
                preformBreeding();
            }
        }

        if(slowTick++ < 20)
            return;
        slowTick = 0;
        ItemStack queen = getQueen();
        Specie specie = BeeItem.of(queen);
        if(specie == null)
            return;
        if(queen.getTag() == null)
            BeeItem.init(queen);

        updateSatisfaction();

        int hp = BeeItem.getHealth(queen.getTag());
        if(hp <= 0) {
            if(checkLock)
                return;
            if(attemptInsert(level, queen, itemHandler, satisfactionCache == Satisfaction.SATISFIED, continuous)) {
                itemHandler.setStackInSlot(2, ItemStack.EMPTY);
                return;
            }
            checkLock = true;
            return;
        }
        if(satisfactionCache != Satisfaction.NOT_WORKING)
            BeeItem.setHealth(queen.getTag(), hp-20);
    }

    public static boolean attemptInsert(Level level, ItemStack queen, ItemStackHandler inv, boolean satisfied, boolean continuous) {
        var optional = BeeLifecycle.getProduceRecipe(level, queen);
        if(optional.isEmpty())
            return true;
        if(queen == null || queen.isEmpty())
            return true;
        BeeProduceRecipe bpr = optional.get();
        ItemStack commonProduce = bpr.getCommonProduce(satisfied);
        ItemStack rareProduce = bpr.getRolledRareProduce(satisfied, RareProduceGene.of(RareProduceGene.get(queen.getTag())).getChance());

        ItemStack princess = BeeLifecycle.clone(queen, bpr.getSpecie().princessItem);
        ItemStack drone = BeeLifecycle.clone(queen, bpr.getSpecie().droneItem);

        if(continuous) {
            if(!inv.getStackInSlot(0).isEmpty())
                return false;
            if(!inv.getStackInSlot(1).isEmpty())
                return false;
            if(!insert(drone, inv, true))
                return false;
            if(!insert(commonProduce, inv, true))
                return false;
            if(!insert(rareProduce, inv, true))
                return false;

            inv.setStackInSlot(0, drone);
            inv.setStackInSlot(1, princess);
        }
        else {
            if(!insert(princess, inv, true))
                return false;
            if(!insert(drone, inv, true))
                return false;
            if(!insert(commonProduce, inv, true))
                return false;
            if(!insert(rareProduce, inv, true))
                return false;

            insert(princess, inv, false);
            insert(drone, inv, false);
        }
        insert(commonProduce, inv, false);
        insert(rareProduce, inv, false);
        return true;
    }

    public static boolean insert(ItemStack stack, ItemStackHandler inv, boolean sim) {
        if(stack.isEmpty())
            return true;
        for(int i = 3; i < inv.getSlots(); i++) {
            stack = inv.insertItem(i, stack, sim);
            if(stack.isEmpty())
                return true;
        }
        return stack.isEmpty();
    }

    @Override
    public void onToggle(ServerPlayer player, int index, boolean value) {
        switch(index) {
            case 0:
                continuous = value;
                setChanged();
                break;
        }
        if(player != null)
            TogglePacket.sync(getBlockPos(), getLevel(), index, value);
    }
}