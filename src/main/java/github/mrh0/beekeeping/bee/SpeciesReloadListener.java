package github.mrh0.beekeeping.bee;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import github.mrh0.beekeeping.Beekeeping;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SpeciesReloadListener {
    public static void register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return Beekeeping.get("species");
            }

            @Override
            public void onResourceManagerReload(@NotNull ResourceManager manager) {
                SpeciesRegistry.INSTANCE.clear();

                manager.listResources("beekeeping/species", id -> id.getPath().endsWith(".json")).forEach((id, resource) -> {
                    try (var reader = resource.openAsReader()) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        // Consume the stream however you want: medium, rare, or well done.
                    } catch (IOException e) {
                        Beekeeping.LOGGER.error("Failed to load species resource \""+id.toString()+"\"", e);
                    }
                });
            }
        });
    }
}
