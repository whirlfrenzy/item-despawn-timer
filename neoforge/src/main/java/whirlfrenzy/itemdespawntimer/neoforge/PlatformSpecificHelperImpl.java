package whirlfrenzy.itemdespawntimer.neoforge;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet){
        PacketDistributor.sendToPlayer(player, packet);
    }
}
