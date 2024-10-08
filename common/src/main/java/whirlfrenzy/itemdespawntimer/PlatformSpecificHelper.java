package whirlfrenzy.itemdespawntimer;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.network.ServerPlayerEntity;
import whirlfrenzy.itemdespawntimer.networking.ItemDataPacket;

import java.nio.file.Path;

public class PlatformSpecificHelper {
    @ExpectPlatform
    public static void sendPacketToPlayer(ServerPlayerEntity player, ItemDataPacket packet){
        throw new AssertionError("Implemented by platform");
    }

    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError("Implemented by platform");
    }

    @ExpectPlatform
    public static boolean isClient() {
        throw new AssertionError("Implemented by platform");
    }
}
