package github.mrh0.beekeeping.network.packet;

import github.mrh0.beekeeping.network.BeekeepingChannel;
import github.mrh0.beekeeping.network.IHasToggleOption;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ToggleServerPacket {
	public static void send(BlockPos pos, Level level, int index, boolean value) {
		FriendlyByteBuf buf = PacketByteBufs.create();

		buf.writeBlockPos(pos);
		buf.writeInt(index);
		buf.writeBoolean(value);

		((ServerLevel) level).players().forEach(player -> ServerPlayNetworking.send(player, BeekeepingChannel.TOGGLE_S2C_ID, buf));
	}

	public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		BlockPos pos = buf.readBlockPos();
		BlockEntity te = player.getLevel().getChunkAt(pos).getBlockEntity(pos); // Level#getBlockEntity returns null if not client-side...?
		if (te != null) {
			if (te instanceof IHasToggleOption to) {
				to.onToggle(player, buf.readInt(), buf.readBoolean());
				Packet<ClientGamePacketListener> teUpdatePacket = te.getUpdatePacket();
				if (teUpdatePacket != null)
					responseSender.sendPacket(teUpdatePacket);
			}
		}
	}
}
