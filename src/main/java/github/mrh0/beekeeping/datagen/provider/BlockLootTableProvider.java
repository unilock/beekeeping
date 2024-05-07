package github.mrh0.beekeeping.datagen.provider;

import github.mrh0.beekeeping.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

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
    }
}
