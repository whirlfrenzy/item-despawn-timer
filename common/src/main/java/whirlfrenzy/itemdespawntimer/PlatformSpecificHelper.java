package whirlfrenzy.itemdespawntimer;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlatformSpecificHelper {
    @ExpectPlatform
    public static void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet){
        throw new AssertionError("Implemented by platform");
    }
}
