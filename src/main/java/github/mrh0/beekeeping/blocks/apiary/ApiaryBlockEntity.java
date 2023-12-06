package github.mrh0.beekeeping.blocks.apiary;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Satisfaction;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.item.frame.FrameItem;
import github.mrh0.beekeeping.item.frame.ProduceEvent;
import github.mrh0.beekeeping.item.frame.SatisfactionEvent;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ApiaryBlockEntity extends BlockEntity implements MenuProvider/*, IHasToggleOption*/ {

    private static final int LIFETIME_STEP = Config.LIFETIME_STEP.get();
    public static final int BREED_TIME = Config.BREED_TIME.get();

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
    private Specie offspringCache = null;

    public ItemStack getDrone() {
        return inputItemHandler.getStackInSlot(0);
    }

    public ItemStack getPrincess() {
        return inputItemHandler.getStackInSlot(1);
    }

    public ItemStack getQueen() {
        return inputItemHandler.getStackInSlot(2);
    }

    public ItemStack getFrame() {
        return inputItemHandler.getStackInSlot(3);
    }

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(4) {
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

		@Override
		public boolean isItemValid(int slot, ItemVariant resource, long amount) {
			if(resource.toStack().is(Index.DRONE_BEES_TAG) && slot == 0)
				return super.isItemValid(slot, resource, amount);
			if(resource.toStack().is(Index.PRINCESS_BEES_TAG) && slot == 1)
				return super.isItemValid(slot, resource, amount);
			if(resource.toStack().is(Index.QUEEN_BEES_TAG) && slot == 2)
				return super.isItemValid(slot, resource, amount);
			if(resource.toStack().is(Index.FRAME_TAG) && slot == 3)
				return super.isItemValid(slot, resource, amount);
			return false;
		}
	};

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
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
        offspringQueen.setTag(BeeLifecycle.getOffspringTag(getDrone(), getPrincess(), offspringCache, Gene::select));
        offspringQueen = FrameItem.onBreed(getFrame(), getLevel(), getBlockPos(), getDrone(), getPrincess(), offspringQueen);

        inputItemHandler.setStackInSlot(0, ItemStack.EMPTY);
        inputItemHandler.setStackInSlot(1, ItemStack.EMPTY);
        inputItemHandler.setStackInSlot(2, offspringQueen);
    }

    public void updateSatisfaction() {
        if(getQueen() == null || getQueen().isEmpty()) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        Specie specie = BeeItem.speciesOf(getQueen());
        if(specie == null) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        lightSatisfactionCache = specie.getLightSatisfaction(getQueen(), getLevel(), getBlockPos());
        weatherSatisfactionCache = specie.getWeatherSatisfaction(getQueen(), getLevel(), getBlockPos());
        temperatureSatisfactionCache = specie.getTemperatureSatisfaction(getQueen(), getLevel(), getBlockPos());

        lightSatisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.LIGHT, getQueen(), lightSatisfactionCache);
        weatherSatisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.WEATHER, getQueen(), weatherSatisfactionCache);
        temperatureSatisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.TEMPERATURE, getQueen(), temperatureSatisfactionCache);

        satisfactionCache = Satisfaction.calc(lightSatisfactionCache, weatherSatisfactionCache, temperatureSatisfactionCache);
        satisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.TOTAL, getQueen(), satisfactionCache);
    }

    public LazyOptional<ItemStackHandler> lazyInputItemHandler = LazyOptional.empty();
	public LazyOptional<ItemStackHandler> lazyOutputItemHandler = LazyOptional.empty();

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.beekeeping.apiary");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new ApiaryMenu(id, inv, this, this.data);
    }

	@Override
    public void onLoad() {
        super.onLoad();
        lazyInputItemHandler = LazyOptional.of(() -> inputItemHandler);
        lazyOutputItemHandler = LazyOptional.of(() -> outputItemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyInputItemHandler.invalidate();
        lazyOutputItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("input", inputItemHandler.serializeNBT());
        tag.put("output", outputItemHandler.serializeNBT());
        tag.putBoolean("continuous", continuous);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inputItemHandler.deserializeNBT(nbt.getCompound("input"));
        outputItemHandler.deserializeNBT(nbt.getCompound("output"));
        continuous = nbt.getBoolean("continuous");
    }

    public void drop() {
        SimpleContainer input = new SimpleContainer(inputItemHandler.getSlots());
        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            input.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, input);

        SimpleContainer output = new SimpleContainer(outputItemHandler.getSlots());
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            output.setItem(i, outputItemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, output);
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
        else
            breedProgressTime = 0;

        if(slowTick++ < 20)
            return;
        slowTick = 0;
        ItemStack queen = getQueen();
        Specie specie = BeeItem.speciesOf(queen);
        if(specie == null)
            return;
        if(queen.getTag() == null)
            BeeItem.init(queen);

        updateSatisfaction();

        int hp = BeeItem.getHealth(queen.getTag());
        if(hp <= 0) {
            if(checkLock)
                return;
            if(attemptInsert(queen, inputItemHandler, outputItemHandler, satisfactionCache == Satisfaction.SATISFIED, continuous)) {
                inputItemHandler.setStackInSlot(2, ItemStack.EMPTY);
                return;
            }
            checkLock = true;
            return;
        }
        if(satisfactionCache != Satisfaction.NOT_WORKING)
            BeeItem.setHealth(queen.getTag(), hp-LIFETIME_STEP);
    }

    public boolean attemptInsert(ItemStack queen, ItemStackHandler input, ItemStackHandler output, boolean satisfied, boolean continuous) {
        var optional = BeeLifecycle.getProduceRecipe(getLevel(), queen);
        if(optional.isEmpty())
            return true;
        if(queen == null || queen.isEmpty())
            return true;

        BeeProduceRecipe bpr = optional.get();
        ItemStack commonProduce = bpr.getCommonProduce(satisfied);
        commonProduce = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.COMMON, commonProduce);

        double chance = RareProduceGene.of(RareProduceGene.get(queen.getTag())).getChance();
        ItemStack rareProduce = bpr.getRolledRareProduce(satisfied, chance);
        rareProduce = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.RARE, rareProduce);

        ItemStack princess = BeeLifecycle.clone(queen, bpr.getSpecie().princessItem);
        princess = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.PRINCESS, princess);

        ItemStack drone = BeeLifecycle.clone(queen, bpr.getSpecie().droneItem);
        drone = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.DRONE, drone);

		Transaction transaction = Transaction.openOuter();

        if(continuous) {
            if(!input.getStackInSlot(0).isEmpty())
                if(!drone.isEmpty() && output.simulateInsert(ItemVariant.of(drone), drone.getCount(), transaction) != drone.getCount())
                    return false;
            if(!input.getStackInSlot(1).isEmpty())
                if(!princess.isEmpty() && output.simulateInsert(ItemVariant.of(princess), princess.getCount(), transaction) != princess.getCount())
                    return false;

            if(!commonProduce.isEmpty() && output.simulateInsert(ItemVariant.of(commonProduce), commonProduce.getCount(), transaction) != commonProduce.getCount())
                return false;
			if(!rareProduce.isEmpty() && output.simulateInsert(ItemVariant.of(rareProduce), rareProduce.getCount(), transaction) != rareProduce.getCount())
				return false;

            if(input.getStackInSlot(0).isEmpty())
                input.setStackInSlot(0, drone);
            else {
				Transaction nested = Transaction.openNested(transaction);
				if(!drone.isEmpty())
					output.insertSlot(0, ItemVariant.of(drone), drone.getCount(), nested);
				nested.commit();
			}

            if(input.getStackInSlot(1).isEmpty())
                input.setStackInSlot(1, princess);
            else {
				Transaction nested = Transaction.openNested(transaction);
				if(!princess.isEmpty())
					output.insertSlot(1, ItemVariant.of(princess), princess.getCount(), nested);
				nested.commit();
			}
        }
        else {
            if(!princess.isEmpty() && output.simulateInsert(ItemVariant.of(princess), princess.getCount(), transaction) != princess.getCount())
                return false;
            if(!drone.isEmpty() && output.simulateInsert(ItemVariant.of(drone), drone.getCount(), transaction) != drone.getCount())
                return false;
			if(!commonProduce.isEmpty() && output.simulateInsert(ItemVariant.of(commonProduce), commonProduce.getCount(), transaction) != commonProduce.getCount())
				return false;
			if(!rareProduce.isEmpty() && output.simulateInsert(ItemVariant.of(rareProduce), rareProduce.getCount(), transaction) != rareProduce.getCount())
				return false;

			Transaction nested = Transaction.openNested(transaction);
			if(!princess.isEmpty())
				output.insert(ItemVariant.of(princess), princess.getCount(), nested);
			if(!drone.isEmpty())
				output.insert(ItemVariant.of(drone), drone.getCount(), nested);
			nested.commit();
        }

		Transaction nested = Transaction.openNested(transaction);
		if(!commonProduce.isEmpty())
			output.insert(ItemVariant.of(commonProduce), commonProduce.getCount(), nested);
		if(!rareProduce.isEmpty())
			output.insert(ItemVariant.of(rareProduce), rareProduce.getCount(), nested);
		nested.commit();

		transaction.commit();
        return true;
    }

   /*
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
	 */
}
