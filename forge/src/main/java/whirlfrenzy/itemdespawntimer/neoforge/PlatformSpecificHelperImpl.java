package whirlfrenzy.itemdespawntimer.neoforge;

import net.minecraft.network.NetworkPhase;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

import java.nio.file.Path;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet){
        if(NetworkRegistry.hasChannel(player.networkHandler.getConnection(), NetworkPhase.PLAY, packet.getId().id())){
            PacketDistributor.sendToPlayer(player, packet);
        }
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }
}
