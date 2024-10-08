package whirlfrenzy.itemdespawntimer.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.networking.ItemDataPacket;

import java.nio.file.Path;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, ItemDataPacket packet){
        if(ServerPlayNetworking.canSend(player, packet.getId())) {
            ServerPlayNetworking.send(player, packet.getId(), packet.writeToBuffer(PacketByteBufs.create()));
        }
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isClient(){
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
