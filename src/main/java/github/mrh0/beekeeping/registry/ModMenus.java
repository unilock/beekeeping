package github.mrh0.beekeeping.registry;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static final MenuType<AnalyzerMenu> ANALYZER = register("analyzer", AnalyzerMenu::new);
    public static final MenuType<ApiaryMenu> APIARY = register("apiary", ApiaryMenu::new);

    public static void init() {

    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String path, ExtendedScreenHandlerType.ExtendedFactory<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, Beekeeping.get(path), new ExtendedScreenHandlerType<>(factory));
    }
}
