package github.mrh0.beekeeping;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import github.mrh0.beekeeping.bee.SpeciesReloadListener;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.network.BeekeepingChannel;
import github.mrh0.beekeeping.registry.ModBlockEntities;
import github.mrh0.beekeeping.registry.ModBlocks;
import github.mrh0.beekeeping.registry.ModItems;
import github.mrh0.beekeeping.registry.ModMenus;
import github.mrh0.beekeeping.registry.ModRecipes;
import github.mrh0.beekeeping.registry.ModTags;
import github.mrh0.beekeeping.world.gen.BeehiveBiomeModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Beekeeping implements ModInitializer {
    public static final String MODID = "beekeeping";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        LOGGER.info("Beekeeping Init!");
        BeekeepingChannel.registerServer();

        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ModBlocks.init();
        ModBlockEntities.init();
        ModItems.init();
        ModMenus.init();
        ModTags.init();
        ModRecipes.init();

        BeehiveBiomeModifier.modify();
        ItemGroup.init();

        SpeciesReloadListener.register();
    }

    public static ResourceLocation get(String resource) {
        return new ResourceLocation(Beekeeping.MODID, resource);
    }
}
