package github.mrh0.beekeeping;

import github.mrh0.beekeeping.config.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Beekeeping implements ModInitializer {
    public static final String MODID = "beekeeping";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
    public void onInitialize() {
		LOGGER.info("Beekeeping Init!");
		ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Index.register();
    }

    public static ResourceLocation get(String resource) {
        return new ResourceLocation(Beekeeping.MODID, resource);
    }
}
