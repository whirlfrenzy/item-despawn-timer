package whirlfrenzy.itemdespawntimer.fabric.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;

public class ServerNetworkingFabric {
    public static void initialize(){
        PayloadTypeRegistry.playS2C().register(SetItemAgePacket.PACKET_ID, SetItemAgePacket.PACKET_CODEC);
    }
}
