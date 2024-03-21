package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();

    public BlockLootTableProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.ANALYZER);
        this.dropSelf(ModBlocks.APIARY);

        for (Species species : SpeciesRegistry.INSTANCE.getAll()) {
            if (species.hasBeehive()) {
                this.add(species.beehive.block, block -> beehiveLootTable(species.beehive.block, species.queenItem, species.princessItem, species.droneItem));
            }
        }
    }

    protected static LootTable.Builder beehiveLootTable(Block beehive, Item queen, Item princess, Item drone) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(beehive)))
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0f))
                    .add(LootItem.lootTableItem(princess).setWeight(2))
                    .add(LootItem.lootTableItem(queen).setWeight(1))
                )
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(princess).setWeight(1))
                        .add(LootItem.lootTableItem(drone).setWeight(3))
                );
    }
}
