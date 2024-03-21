package github.mrh0.beekeeping;

import github.mrh0.beekeeping.event.ClientOverlay;
import github.mrh0.beekeeping.network.BeekeepingChannel;
import github.mrh0.beekeeping.registry.ModMenus;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerScreen;
import github.mrh0.beekeeping.screen.apiary.ApiaryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;

public class BeekeepingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BeekeepingChannel.registerClient();

        MenuScreens.register(ModMenus.ANALYZER, AnalyzerScreen::new);
        MenuScreens.register(ModMenus.APIARY, ApiaryScreen::new);

        HudRenderCallback.EVENT.register(ClientOverlay::renderOverlay);
    }
}
