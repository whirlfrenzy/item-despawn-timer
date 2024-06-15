package whirlfrenzy.itemdespawntimer.fabric;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet){
        if(ServerPlayNetworking.canSend(player, packet.getId())) {
            ServerPlayNetworking.send(player, packet);
        }
    }
}
