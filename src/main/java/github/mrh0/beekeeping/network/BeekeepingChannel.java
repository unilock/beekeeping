package github.mrh0.beekeeping.network;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.network.packet.ToggleClientPacket;
import github.mrh0.beekeeping.network.packet.ToggleServerPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class BeekeepingChannel {
    public static final ResourceLocation TOGGLE_S2C_ID = Beekeeping.get("toggle_s2c");
    public static final ResourceLocation TOGGLE_C2S_ID = Beekeeping.get("toggle_c2s");

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(TOGGLE_S2C_ID, ToggleClientPacket::receive);
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_C2S_ID, ToggleServerPacket::receive);
    }
}
