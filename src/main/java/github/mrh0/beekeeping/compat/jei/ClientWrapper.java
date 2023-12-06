package github.mrh0.beekeeping.compat.jei;

import net.minecraft.client.Minecraft;

public class ClientWrapper {
	static Minecraft get() {
		return Minecraft.getInstance();
	}
}
