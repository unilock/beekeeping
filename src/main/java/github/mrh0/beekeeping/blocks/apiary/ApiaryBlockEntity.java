package github.mrh0.beekeeping.blocks.apiary;

import github.mrh0.beekeeping.bee.Satisfaction;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.item.frame.FrameItem;
import github.mrh0.beekeeping.item.frame.ProduceEvent;
import github.mrh0.beekeeping.item.frame.SatisfactionEvent;
import github.mrh0.beekeeping.network.IHasToggleOption;
import github.mrh0.beekeeping.network.packet.ToggleServerPacket;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.registry.ModBlockEntities;
import github.mrh0.beekeeping.registry.ModTags;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ApiaryBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, IHasToggleOption {

    private static final int LIFETIME_STEP = Config.LIFETIME_STEP.get();
    public static final int BREED_TIME = Config.BREED_TIME.get();

    protected final ContainerData data;
    public ApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.APIARY, pos, state);
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
    private Species offspringCache = null;

    public ItemStack getDrone() {
        return inputContainer.getItem(0);
    }

    public ItemStack getPrincess() {
        return inputContainer.getItem(1);
    }

    public ItemStack getQueen() {
        return inputContainer.getItem(2);
    }

    public ItemStack getFrame() {
        return inputContainer.getItem(3);
    }

    public final SimpleContainer inputContainer = new SimpleContainer(4) {
        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            if (stack.is(ModTags.Items.DRONES) && index == 0) {
                return super.canPlaceItem(index, stack);
            }

            if (stack.is(ModTags.Items.PRINCESSES) && index == 1) {
                return super.canPlaceItem(index, stack);
            }

            if (stack.is(ModTags.Items.QUEENS) && index == 2) {
                return super.canPlaceItem(index, stack);
            }

            if (stack.is(ModTags.Items.FRAMES) && index == 3) {
                return super.canPlaceItem(index, stack);
            }

            return false;
        }

        @Override
        public void setChanged() {
            checkLock = false;

            if (getLevel() != null && !getLevel().isClientSide) {
                breedCheck();
            }

            ApiaryBlockEntity.this.setChanged();

            super.setChanged();
        }
    };

    public final SimpleContainer outputContainer = new SimpleContainer(6) {
        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            return false;
        }

        @Override
        public void setChanged() {
            checkLock = false;

            super.setChanged();

            ApiaryBlockEntity.this.setChanged();
        }
    };

    public final InventoryStorage inputInventoryWrapper = InventoryStorage.of(inputContainer, null);

    public final InventoryStorage outputInventoryWrapper = InventoryStorage.of(outputContainer, null);

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

        if(drone.getTag() == null || !drone.getTag().contains(BeeItem.HEALTH_KEY))
            BeeItem.init(drone);
        if(princess.getTag() == null || !princess.getTag().contains(BeeItem.HEALTH_KEY))
            BeeItem.init(princess);

        offspringCache = BeeLifecycle.getOffspringSpecies(getLevel(), drone, princess);
    }

    private void preformBreeding() {
        if(offspringCache == null)
            return;

        ItemStack offspringQueen = offspringCache.getQueen();
        offspringQueen.setTag(BeeLifecycle.getOffspringTag(getDrone(), getPrincess(), offspringCache, Gene::select));
        offspringQueen = FrameItem.onBreed(getFrame(), getLevel(), getBlockPos(), getDrone(), getPrincess(), offspringQueen);

        inputContainer.setItem(0, ItemStack.EMPTY);
        inputContainer.setItem(1, ItemStack.EMPTY);
        inputContainer.setItem(2, offspringQueen);
    }

    public void updateSatisfaction() {
        if(getQueen() == null || getQueen().isEmpty()) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        Species species = BeeItem.getSpecies(getQueen());
        if(species == null) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        lightSatisfactionCache = species.getLightSatisfaction(getQueen(), getLevel(), getBlockPos());
        weatherSatisfactionCache = species.getWeatherSatisfaction(getQueen(), getLevel(), getBlockPos());
        temperatureSatisfactionCache = species.getTemperatureSatisfaction(getQueen(), getLevel(), getBlockPos());

        lightSatisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.LIGHT, getQueen(), lightSatisfactionCache);
        weatherSatisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.WEATHER, getQueen(), weatherSatisfactionCache);
        temperatureSatisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.TEMPERATURE, getQueen(), temperatureSatisfactionCache);

        satisfactionCache = Satisfaction.calc(lightSatisfactionCache, weatherSatisfactionCache, temperatureSatisfactionCache);
        satisfactionCache = FrameItem.onSatisfaction(getFrame(), getLevel(), getBlockPos(), SatisfactionEvent.SatisfactionType.TOTAL, getQueen(), satisfactionCache);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.beekeeping.apiary");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new ApiaryMenu(id, inv, this, this.data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("input", inputContainer.createTag());
        tag.put("output", outputContainer.createTag());
        tag.putBoolean("continuous", continuous);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inputContainer.fromTag(nbt.getList("input", Tag.TAG_LIST));
        outputContainer.fromTag(nbt.getList("output", Tag.TAG_LIST));
        continuous = nbt.getBoolean("continuous");
    }

    public void drop() {
        Containers.dropContents(this.level, this.worldPosition, inputContainer);
        Containers.dropContents(this.level, this.worldPosition, outputContainer);
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
        if(getLevel() == null || getLevel().isClientSide())
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
        Species species = BeeItem.getSpecies(queen);
        if (species == null) {
            return;
        }
        if (queen.getTag() == null || !queen.getTag().contains(BeeItem.HEALTH_KEY)) {
            BeeItem.init(queen);
        }

        updateSatisfaction();

        int hp = BeeItem.getHealth(queen);

        if (hp <= 0) {
            if (checkLock) {
                return;
            }
            if (attemptInsert(queen, inputInventoryWrapper, outputInventoryWrapper, satisfactionCache == Satisfaction.SATISFIED, continuous)) {
                inputContainer.setItem(2, ItemStack.EMPTY);
                return;
            }
            checkLock = true;
            return;
        }

        if (satisfactionCache != Satisfaction.NOT_WORKING) {
            BeeItem.setHealth(queen, hp - LIFETIME_STEP);
        }
    }

    public boolean attemptInsert(ItemStack queen, InventoryStorage input, InventoryStorage output, boolean satisfied, boolean continuous) {
        var optional = BeeLifecycle.getProduceRecipe(getLevel(), queen);
        if (optional.isEmpty() || queen == null || queen.isEmpty()) {
            return true;
        }

        BeeProduceRecipe bpr = optional.get();
        ItemStack commonProduce = bpr.getCommonProduce(satisfied);
        commonProduce = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.COMMON, commonProduce);

        double chance = RareProduceGene.of(RareProduceGene.get(queen)).getChance();
        ItemStack rareProduce = bpr.getRolledRareProduce(satisfied, chance);
        rareProduce = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.RARE, rareProduce);

        ItemStack princess = BeeLifecycle.clone(queen, bpr.getSpecies().getPrincess());
        princess = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.PRINCESS, princess);

        ItemStack drone = BeeLifecycle.clone(queen, bpr.getSpecies().getDrone());
        drone = FrameItem.onProduce(getFrame(), getLevel(), getBlockPos(), ProduceEvent.ProduceType.DRONE, drone);

        try (Transaction transaction = Transaction.openOuter()) {
            if (continuous) {
                if (!actuallyAttemptInsertSlot(transaction, input, 0, drone)) return false;
                if (!actuallyAttemptInsertSlot(transaction, input, 1, princess)) return false;
            } else {
                if (!actuallyAttemptInsert(transaction, output, princess)) return false;
                if (!actuallyAttemptInsert(transaction, output, drone)) return false;
            }

            if (!actuallyAttemptInsert(transaction, output, commonProduce)) return false;
            if (!actuallyAttemptInsert(transaction, output, rareProduce)) return false;

            transaction.commit();

            return true;
        }
    }

    private boolean actuallyAttemptInsert(Transaction transaction, InventoryStorage storage, ItemStack stack) {
        try (Transaction nested = Transaction.openNested(transaction)) {
            if (!stack.isEmpty()) {
                int count = stack.getCount();
                long amount = storage.insert(ItemVariant.of(stack), count, nested);
                if (amount == count) {
                    nested.commit();
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    private boolean actuallyAttemptInsertSlot(Transaction transaction, InventoryStorage storage, int slot, ItemStack stack) {
        try (Transaction nested = Transaction.openNested(transaction)) {
            if (!stack.isEmpty()) {
                int count = stack.getCount();
                long amount = storage.getSlot(slot).insert(ItemVariant.of(stack), count, nested);
                if (amount == count) {
                    nested.commit();
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onToggle(ServerPlayer player, int index, boolean value) {
        switch (index) {
            case 0:
                continuous = value;
                checkLock = false;
                setChanged();
                break;
        }
        if (player != null) {
            ToggleServerPacket.send(getBlockPos(), getLevel(), index, value);
        }
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.getBlockPos());
    }
}
