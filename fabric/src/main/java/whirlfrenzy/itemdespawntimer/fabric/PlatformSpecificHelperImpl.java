package whirlfrenzy.itemdespawntimer.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

import java.nio.file.Path;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet){
        if(ServerPlayNetworking.canSend(player, packet.getId())) {
            ServerPlayNetworking.send(player, packet);
        }
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isClient(){
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
