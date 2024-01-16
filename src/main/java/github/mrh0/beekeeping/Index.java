package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlock;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlock;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.item.ItemBuilder;
import github.mrh0.beekeeping.item.ThermometerItem;
import github.mrh0.beekeeping.item.frame.FrameItem;
import github.mrh0.beekeeping.item.frame.ProduceEvent;
import github.mrh0.beekeeping.item.frame.SatisfactionEvent;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import github.mrh0.beekeeping.world.gen.BeehiveBiomeModifier;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;

public class Index {
    public static final LazyRegistrar<Item> ITEMS =
            LazyRegistrar.create(Registries.ITEM, Beekeeping.MODID);
    public static final LazyRegistrar<Block> BLOCKS =
            LazyRegistrar.create(Registries.BLOCK, Beekeeping.MODID);
    public static final LazyRegistrar<BlockEntityType<?>> BLOCK_ENTITIES =
            LazyRegistrar.create(Registries.BLOCK_ENTITY_TYPE, Beekeeping.MODID);
    public static final LazyRegistrar<RecipeSerializer<?>> SERIALIZERS =
            LazyRegistrar.create(Registries.RECIPE_SERIALIZER, Beekeeping.MODID);
    public static final LazyRegistrar<RecipeType<?>> TYPES =
        LazyRegistrar.create(Registries.RECIPE_TYPE, Beekeeping.MODID);

    static {
        species();
        blocks();
        items();
        blockEntities();
        menus();
        tags();
        recipes();
    }

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
        BLOCK_ENTITIES.register();
        SERIALIZERS.register();
        TYPES.register();
        BeehiveBiomeModifier.modify();
        ItemGroup.register();
    }

    private static TagKey<Item> bind(String key) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(key));
    }

    //  SPECIE
    public static void species() {
        var r = SpeciesRegistry.instance;
        r.register(new Specie("common", 0xFFfff2cc)
                .setProduce(Items.HONEYCOMB, 3, 5)
                .addBeehive(Tags.Biomes.IS_PLAINS, Config.BEEHIVE_COMMON_TRIES.get(), Config.BEEHIVE_COMMON_RARITY.get()));
        r.register(new Specie("forest", 0xFF93c47d)
                .setProduce(Items.HONEYCOMB, 3, 5)
                .addBeehive(BiomeTags.IS_FOREST, Config.BEEHIVE_FOREST_TRIES.get(), Config.BEEHIVE_FOREST_RARITY.get()));

        r.register(new Specie("tempered", 0xFFb6d7a8)
                .setProduce(Items.HONEYCOMB, 7, 9)
                .setTemperatureGene(Gene::random5Narrow)
                .breedFrom("common", "forest"));

        r.register(new Specie("tropical", 0xFF6aa84f)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(BiomeTags.IS_JUNGLE, Config.BEEHIVE_TROPICAL_TRIES.get(), Config.BEEHIVE_TROPICAL_RARITY.get())
                .setLifetimeGene(Gene::random5Narrow)
                .setPreferredTemperature(BiomeTemperature.WARM));

        r.register(new Specie("coco", 0xFF783f04)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.COCOA_BEANS, 3, 7)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .breedFrom("tropical", "dugout"));

        r.register(new Specie("upland", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 3, 7, Items.HONEY_BLOCK, 1, 2)
                .addBeehive(BiomeTags.IS_SAVANNA, Config.BEEHIVE_UPLAND_TRIES.get(), Config.BEEHIVE_UPLAND_RARITY.get())
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Specie("dune", 0xFFfbbc04)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(Tags.Biomes.IS_SANDY, Config.BEEHIVE_DUNE_TRIES.get(), Config.BEEHIVE_DUNE_RARITY.get()) // TODO: Fix biome tag sandy & hot
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Specie("snowy", 0xFFefefef)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SNOWBALL, 8, 16)
                .addBeehive(Tags.Biomes.IS_SNOWY, Config.BEEHIVE_SNOWY_TRIES.get(), Config.BEEHIVE_SNOWY_RARITY.get())
                .setTemperatureGene(Gene::random3Low)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Specie("frozen", 0xFFd0e0e3)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.ICE, 3, 7)
                .setPreferredTemperature(BiomeTemperature.COLD)
                .breedFrom("snowy", "dugout"));

        r.register(new Specie("glacial", 0xFFa2c4c9)
                .setFoil()
                .setProduce(Items.HONEYCOMB, 5, 7, Items.PACKED_ICE, 3, 7)
                .setPreferredTemperature(BiomeTemperature.COLDEST)
                .breedFrom("frozen", "nocturnal"));

        r.register(new Specie("fungal", 0xFF660000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.RED_MUSHROOM, 0.5d, 0.8d)
                .addBeehive(Tags.Biomes.IS_MUSHROOM, Config.BEEHIVE_FUNGAL_TRIES.get(), Config.BEEHIVE_FUNGAL_RARITY.get()) // TODO: fix biome tag mushroom & swamp
                .setTemperatureGene(Gene::random3High)
                .setProduceGene(Gene::random5High));

        r.register(new Specie("mossy", 0xFF38761d)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.MOSS_BLOCK, 0.5d, 0.8d)
                .setTemperatureGene(Gene::random3Low)
                .setProduceGene(Gene::normal5)
                .breedFrom("dugout", "fungal"));

        r.register(new Specie("fair", 0xFF00ff00)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SUGAR, 8, 16)
                .breedFrom("coco", "snowy")
                .setFoil());

        r.register(new Specie("dugout", 0xFF7f6000)
                .setProduce(Items.HONEYCOMB, 5, 7)
                .addBeehive(BiomeTags.IS_OVERWORLD,
                        Config.BEEHIVE_DUGOUT_TRIES.get(), Config.BEEHIVE_DUGOUT_RARITY.get(), PlacementUtils.FULL_RANGE, Feature.RANDOM_PATCH,
                        pos -> pos.getY() < Config.BEEHIVE_DUGOUT_MAX_HEIGHT.get() && pos.getY() > Config.BEEHIVE_DUGOUT_MIN_HEIGHT.get())
                .setTemperatureGene(Gene::random3High)
                .setLightGene(Gene::any)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Specie("nocturnal", 0xFF073763)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.GLOWSTONE_DUST, 0.5d, 0.8d)
                .setNocturnal()
                .breedFrom("ender", "dugout"));

        r.register(new Specie("malignant", 0xFF999999)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.BONE_MEAL, 3, 7)
                .addBeehive(Tags.Biomes.IS_WASTELAND, Config.BEEHIVE_MALIGNANT_TRIES.get(), Config.BEEHIVE_MALIGNANT_RARITY.get()) // TODO: fix biome tag mesa & wasteland
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark());

        r.register(new Specie("wicked", 0xFF666666)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.SPIDER_EYE, 1, 3)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark()
                .breedFrom("malignant", "upland"));

        r.register(new Specie("withered", 0xFF434343)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.WITHER_SKELETON_SKULL, 0.02, 0.05)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .setDark()
                .setFoil()
                .breedFrom("wicked", "demonic"));

        r.register(new Specie("scorched", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.COAL, 2, 5)
                .addBeehive(BiomeTags.IS_NETHER, Config.BEEHIVE_SCORCHED_TRIES.get(), Config.BEEHIVE_SCORCHED_RARITY.get(),
                        PlacementUtils.FULL_RANGE, Feature.RANDOM_PATCH,
                        pos -> pos.getY() < Config.BEEHIVE_SCORCHED_MAX_HEIGHT.get() && pos.getY() > Config.BEEHIVE_SCORCHED_MIN_HEIGHT.get())
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark());

        r.register(new Specie("magmatic", 0xFFff6d01)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.MAGMA_CREAM, 0.5d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("scorched", "dune"));

        r.register(new Specie("infernal", 0xFFff0000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.GUNPOWDER, 1, 3)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("magmatic", "wicked"));

        r.register(new Specie("demonic", 0xFF990000)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.BLAZE_POWDER, 0.5d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setFoil()
                .setDark()
                .breedFrom("infernal", "nocturnal"));

        r.register(new Specie("ender", 0xFF134f5c)
                .setProduce(Items.HONEYCOMB, 5, 7, Items.ENDER_PEARL, 0.2d, 0.4d)
                .addBeehive(BiomeTags.IS_END, Config.BEEHIVE_ENDER_TRIES.get(), Config.BEEHIVE_ENDER_RARITY.get())
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene::any)
                .setDark());
    }

    //  ITEM
    public static RegistryObject<ThermometerItem> THERMOMETER;
    public static RegistryObject<FrameItem> BASIC_FRAME;

    public static void items() {
        THERMOMETER = ITEMS.register("thermometer", () -> new ItemBuilder<>(new  ThermometerItem())
                .shapeless(1, ANALYZER_BLOCK.get(), Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.GLASS_PANE), Ingredient.of(Items.REDSTONE), Ingredient.of(Items.GLASS_PANE))
                .build());

        BASIC_FRAME = ITEMS.register("basic_frame", () -> new FrameItem("basic"));
        ITEMS.register("glowing_frame", () -> new ItemBuilder<>(new FrameItem("glowing")
                .addSatisfactionEvent(((level, pos, type, queen, satisfaction) ->
                    type == SatisfactionEvent.SatisfactionType.LIGHT ? satisfaction.up() : satisfaction)))
                .shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(Items.GLOWSTONE_DUST))
                //.shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(Items.GLOW_BERRIES))
                .build());

        ITEMS.register("water_proof_frame", () -> new ItemBuilder<>(new FrameItem("water_proof")
                .addSatisfactionEvent(((level, pos, type, queen, satisfaction) ->
                        type == SatisfactionEvent.SatisfactionType.WEATHER ? satisfaction.up() : satisfaction)))
                .shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(Items.DRIED_KELP))
                .build());

        ITEMS.register("insulated_frame", () -> new ItemBuilder<>(new FrameItem("insulated")
                .addSatisfactionEvent(((level, pos, type, queen, satisfaction) ->
                        type == SatisfactionEvent.SatisfactionType.TEMPERATURE ? satisfaction.up() : satisfaction)))
                .shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(ItemTags.WOOL))
                .build());

        ITEMS.register("mutation_frame", () -> new ItemBuilder<>(new FrameItem("mutation")
                .addProduceEvent((level, pos, type, stack) ->
                        type == ProduceEvent.ProduceType.DRONE || type == ProduceEvent.ProduceType.PRINCESS
                                ? BeeLifecycle.mutateRandom(stack)
                                : stack))
                .shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(bind("c:raw_uranium_ores")))
                .build());

        /*ITEMS.register("cursed_frame", () -> new ItemBuilder<>(new FrameItem("cursed")
                .addBreedEvent((level, pos, drone, princess, queen) -> {
                    queen.setTag(BeeLifecycle.getOffspringTag(drone, princess, BeeItem.speciesOf(queen), Math::min));
                    return queen;
                }))
                .shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(ItemTags.SOUL_FIRE_BASE_BLOCKS))
                .build());

        ITEMS.register("blessed_frame", () -> new ItemBuilder<>(new FrameItem("blessed")
                .addBreedEvent((level, pos, drone, princess, queen) -> {
                    queen.setTag(BeeLifecycle.getOffspringTag(drone, princess, BeeItem.speciesOf(queen), Math::max));
                    return queen;
                }))
                .shapeless(1, BASIC_FRAME.get(), Ingredient.of(BASIC_FRAME.get()), Ingredient.of(Items.GLISTERING_MELON_SLICE))
                .build());
        */
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            ITEMS.register(specie.getName() + "_drone", specie::buildDroneItem);
            ITEMS.register(specie.getName() + "_princess", specie::buildPrincessItem);
            ITEMS.register(specie.getName() + "_queen", specie::buildQueenItem);
        }
    }

    //  BLOCK
    public static RegistryObject<AnalyzerBlock> ANALYZER_BLOCK;
    public static RegistryObject<ApiaryBlock> APIARY_BLOCK;

    public static void blocks() {
        ANALYZER_BLOCK = BLOCKS.register("analyzer", AnalyzerBlock::new);
        ITEMS.register("analyzer", () -> new BlockItem(ANALYZER_BLOCK.get(), new Item.Properties()));
        APIARY_BLOCK = BLOCKS.register("apiary", ApiaryBlock::new);
        ITEMS.register("apiary", () -> new BlockItem(APIARY_BLOCK.get(), new Item.Properties()));

        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(!specie.hasBeehive())
                continue;
            specie.beehive.block = BLOCKS.register(specie.beehive.getName(), () -> new BeehiveBlock(BlockBehaviour.Properties.copy(Blocks.BEEHIVE), specie));
            ITEMS.register(specie.beehive.getName(), () -> new BlockItem(specie.beehive.block.get(), new Item.Properties()));
        }
    }

    //  BLOCK ENTITY
    public static RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER_BLOCK_ENTITY;
    public static RegistryObject<BlockEntityType<ApiaryBlockEntity>> APIARY_BLOCK_ENTITY;

    public static void blockEntities() {
        ANALYZER_BLOCK_ENTITY = BLOCK_ENTITIES.register("analyzer_block_entity", () ->
                BlockEntityType.Builder.of(AnalyzerBlockEntity::new, ANALYZER_BLOCK.get()).build(null));
        APIARY_BLOCK_ENTITY = BLOCK_ENTITIES.register("apiary_block_entity", () ->
                BlockEntityType.Builder.of(ApiaryBlockEntity::new, APIARY_BLOCK.get()).build(null));
    }

    //   MENU
    public static MenuType<AnalyzerMenu> ANALYZER_MENU;
    public static MenuType<ApiaryMenu> APIARY_MENU;

    public static void menus() {
        ANALYZER_MENU = Registry.register(BuiltInRegistries.MENU, Beekeeping.get("analyzer"), new ExtendedScreenHandlerType<>(AnalyzerMenu::new));
        APIARY_MENU = Registry.register(BuiltInRegistries.MENU, Beekeeping.get("apiary"), new ExtendedScreenHandlerType<>(ApiaryMenu::new));
    }

    //  TAG
    public static TagKey<Item> BEES_TAG;
    public static TagKey<Item> DRONE_BEES_TAG;
    public static TagKey<Item> PRINCESS_BEES_TAG;
    public static TagKey<Item> QUEEN_BEES_TAG;
    public static TagKey<Item> FRAME_TAG;
    public static TagKey<Block> BEEHIVE_TAG;

    public static void tags() {
        BEES_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("beekeeping", "bees"));
        DRONE_BEES_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("beekeeping", "drone_bees"));
        PRINCESS_BEES_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("beekeeping", "princess_bees"));
        QUEEN_BEES_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("beekeeping", "queen_bees"));
        FRAME_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("beekeeping", "frames"));
        BEEHIVE_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation("beekeeping", "beehives"));
    }

    //  RECIPE
    public static RegistryObject<RecipeSerializer<BeeBreedingRecipe>> BEE_BREEDING_RECIPE;
    public static RegistryObject<RecipeSerializer<BeeProduceRecipe>> BEE_PRODUCE_RECIPE;
    public static RegistryObject<RecipeType<BeeBreedingRecipe>> BEE_BREEDING_TYPE;
    public static RegistryObject<RecipeType<BeeProduceRecipe>> BEE_PRODUCE_TYPE;

    public static void recipes() {
        BEE_BREEDING_RECIPE = SERIALIZERS.register("bee_breeding", () -> BeeBreedingRecipe.Serializer.INSTANCE);
        BEE_PRODUCE_RECIPE = SERIALIZERS.register("bee_produce", () -> BeeProduceRecipe.Serializer.INSTANCE);
        BEE_BREEDING_TYPE = TYPES.register("bee_breeding", () -> BeeBreedingRecipe.Type.INSTANCE);
        BEE_PRODUCE_TYPE = TYPES.register("bee_produce", () -> BeeProduceRecipe.Type.INSTANCE);
    }
}
