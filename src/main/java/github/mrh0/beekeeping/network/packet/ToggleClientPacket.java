package github.mrh0.beekeeping.network.packet;

import github.mrh0.beekeeping.network.BeekeepingChannel;
import github.mrh0.beekeeping.network.IHasToggleOption;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ToggleClientPacket {
	public static void send(BlockPos pos, Level level, int index, boolean value) {
		FriendlyByteBuf buf = PacketByteBufs.create();

		buf.writeBlockPos(pos);
		buf.writeInt(index);
		buf.writeBoolean(value);

		ClientPlayNetworking.send(BeekeepingChannel.TOGGLE_C2S_ID, buf);
	}

	public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		BlockPos pos = buf.readBlockPos();
		BlockEntity te = handler.getLevel().getChunkAt(pos).getBlockEntity(pos);
		if (te != null) {
			if (te instanceof IHasToggleOption to) {
				to.onToggle(null, buf.readInt(), buf.readBoolean());
			}
		}
	}
}
