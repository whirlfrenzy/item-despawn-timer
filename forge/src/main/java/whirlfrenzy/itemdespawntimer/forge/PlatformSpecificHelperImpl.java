package whirlfrenzy.itemdespawntimer.forge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import whirlfrenzy.itemdespawntimer.forge.networking.ForgeNetworking;
import whirlfrenzy.itemdespawntimer.networking.ItemDataPacket;

import java.nio.file.Path;

public class PlatformSpecificHelperImpl {
    public static void sendPacketToPlayer(ServerPlayerEntity player, ItemDataPacket packet){
        ForgeNetworking.simpleChannelInstance.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }
}
