package github.mrh0.beekeeping;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.network.BeekeepingChannel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
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
        Index.register();

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.lazyItemHandler.getValueUnsafer(), Index.ANALYZER_BLOCK_ENTITY.get());
		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> Direction.UP.equals(direction) ? blockEntity.lazyInputItemHandler.getValueUnsafer() : blockEntity.lazyOutputItemHandler.getValueUnsafer(), Index.APIARY_BLOCK_ENTITY.get());
    }

    public static ResourceLocation get(String resource) {
        return new ResourceLocation(Beekeeping.MODID, resource);
    }
}
