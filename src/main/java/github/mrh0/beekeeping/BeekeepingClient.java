package github.mrh0.beekeeping;

import github.mrh0.beekeeping.event.ClientOverlay;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerScreen;
import github.mrh0.beekeeping.screen.apiary.ApiaryScreen;
import io.github.fabricators_of_create.porting_lib.event.client.OverlayRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class BeekeepingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(Index.ANALYZER_MENU, AnalyzerScreen::new);
		MenuScreens.register(Index.APIARY_MENU, ApiaryScreen::new);

		OverlayRenderCallback.EVENT.register(ClientOverlay::renderOverlay);
	}
}
