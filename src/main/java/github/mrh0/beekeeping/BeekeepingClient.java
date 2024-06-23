package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.event.ClientOverlay;
import github.mrh0.beekeeping.network.BeekeepingChannel;
import github.mrh0.beekeeping.registry.ModItems;
import github.mrh0.beekeeping.registry.ModMenus;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerScreen;
import github.mrh0.beekeeping.screen.apiary.ApiaryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;

import java.util.Objects;

public class BeekeepingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BeekeepingChannel.registerClient();

        MenuScreens.register(ModMenus.ANALYZER, AnalyzerScreen::new);
        MenuScreens.register(ModMenus.APIARY, ApiaryScreen::new);

        HudRenderCallback.EVENT.register(ClientOverlay::renderOverlay);

        ColorProviderRegistry.ITEM.register((stack, tint) -> {
            if (stack.getItem() instanceof BeeItem && Objects.nonNull(BeeItem.getSpecies(stack))) {
                return BeeItem.getSpecies(stack).color;
            } else {
                return 0;
            }
        }, ModItems.DRONE, ModItems.PRINCESS, ModItems.QUEEN);
    }
}
