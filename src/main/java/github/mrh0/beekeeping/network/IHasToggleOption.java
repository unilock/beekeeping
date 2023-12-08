package github.mrh0.beekeeping.network;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface IHasToggleOption {
    void onToggle(@Nullable ServerPlayer player, int index, boolean value);
}
