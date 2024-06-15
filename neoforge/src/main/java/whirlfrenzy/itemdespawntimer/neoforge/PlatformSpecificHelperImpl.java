package whirlfrenzy.itemdespawntimer.neoforge;

import net.minecraft.network.NetworkPhase;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet){
        if(NetworkRegistry.hasChannel(player.networkHandler.getConnection(), NetworkPhase.PLAY, packet.getId().id())){
            PacketDistributor.sendToPlayer(player, packet);
        }
    }
}
